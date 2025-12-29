package journal;

import java.util.Map;

public class mood {
    
    public static String getMood(String journalInput, String bearerToken) throws Exception{
       API api = new API();
       
       String postURL = "https://router.huggingface.co/hf-inference/models/distilbert/distilbert-base-uncased-finetuned-sst-2-english";
       String jsonBody = "{\"inputs\":\"" + journalInput + "\"}";
       
       String respone = api.post(postURL, bearerToken, jsonBody);
       
       String label = extractLabel(respone);
       double score = extractScore(respone);
       
       return mapMood(label,score);
    }
    
    
    private static String extractLabel(String json){
        String key = "\"label\":\"";
        int start = json.indexOf(key) + key.length();
        int end = json.indexOf("\"", start);
        
        if(start < key.length() || end == -1){
            return "Unknown";
        }
        return json.substring(start, end);
    }
    
    private static double extractScore(String json){
        String key = "\"score\":";
        int start = json.indexOf(key) + key.length();
        int end = json.indexOf("}",start);
        
        if(start < key.length() || end == -1){
            return 0.0;
        }
        return Double.parseDouble(json.substring(start,end));
                
    }
    
    private static String mapMood(String label, double score){
        if(label == null)
            return "Unknown";
        
        label = label.toUpperCase();
        
        if(score < 0.6){
            return "Neutral";
        }
        
        switch(label){
            case "POSITIVE" : return "Happy";
            case "NEGATIVE" : return "Sad";
            default : return "Neutral";
        }
    }
    
    public static void main(String[] args){
        try{
           Map<String, String>env = EnvLoader.loadEnv(".env");
           String token = env.get("BEARER_TOKEN");
           if(token == null || token.isEmpty()){
               System.err.println("Error : BEARER_TOKEN missing in .env file");
               return;
           }
           
           String journalInput = "Today I learned how to create a simple terminal journal app!";
           String mood = getMood(journalInput, token);
           
            System.out.println("Your Mood : " + mood);
           
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
        
        
    


   

        