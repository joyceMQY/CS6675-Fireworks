package weibo4j.examples.timeline;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import weibo4j.Timeline;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;


public class GetPublicTimeline extends Thread{
    DB db = null;
	DBCollection postColl = null;
	DBCollection summaryColl = null;
	int total;
	int withGeo;
	long interval;
	
	String access_token;
	
	public GetPublicTimeline(String access_token, long interval){
		try {
			this.access_token = access_token;
			this.interval = interval;
			MongoClient client = new MongoClient("localhost", 27017);
			db = client.getDB("cs6675");
			postColl = db.getCollection("posts");
			summaryColl = db.getCollection("statis");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		Timeline tm = new Timeline();
		tm.client.setToken(access_token);
		int round = 0;
		try {
			while(true){
				StatusWapper status = tm.getPublicTimeline(200, 0);
				for(Status s : status.getStatuses()){
					total++;
					
					if(!s.getGeo().equals("null")){
						withGeo++;
						
						BasicDBObject doc = new BasicDBObject("id", s.getId())
						                        .append("text", s.getText())
						                        .append("source", s.getSource().toString())
						                        .append("reposts_count", s.getRepostsCount())
						                        .append("comments_count", s.getCommentsCount())
						                        .append("latitude", s.getLatitude())
						                        .append("longitude", s.getLongitude())
						                        .append("created_at", s.getCreatedAt().toString());
					
						postColl.insert(doc);
						
					   }
					
				}
				
				// Summary information
				BasicDBObject doc2 = new BasicDBObject("total", total)
						.append("withGeo", withGeo);
				summaryColl.insert(doc2);
				
				System.out.println(access_token + ", round: " + round++);
				Thread.sleep(interval);
			}
		} catch (WeiboException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		if(args.length < 3){
			System.out.println("Usage: java weibo4j.examples.timeline.GetPublicTimeline <token_file> <user_interval> <query_interval>");
			System.out.println("<token_file>: file to save the access_token for different users");
			System.out.println("<user_interval>: how long (ms) is the interval between two users");
			System.out.println("<query_interval>: how long (ms) is the interval between two queries for one user");
			return;
		}
		
		BufferedReader br = null;
		try{
			String line;
			br = new BufferedReader(new FileReader(args[0]));
			while((line = br.readLine()) != null){
				// For account "godmiev587@163.com"
				String access_token = line;
				GetPublicTimeline t = new GetPublicTimeline(access_token, Long.parseLong(args[2]));
				t.start();
				
				try {
					Thread.sleep(Long.parseLong(args[1]));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}catch(IOException e){
			try{
				if(br != null){
					br.close();
				}
			}catch(IOException ex){
				ex.printStackTrace();
			}
		}
		
		// For account "godmiev587@163.com"
//		String access_token = "2.00b3pWYFZSUU9D18cc07179bR8xoFB";
//		GetPublicTimeline t = new GetPublicTimeline(access_token);
//		t.start();
//		
//		try {
//			Thread.sleep(15000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		
//		// For account "hothotplaces2014@gmail.com"
//		access_token = "2.00aPsHYFZSUU9D9647069694uRXXfC";
//		GetPublicTimeline t1 = new GetPublicTimeline(access_token);
//		t1.start();
		
		
		/////////////////////
		//db.posts.distinct('id').length
	}

}
