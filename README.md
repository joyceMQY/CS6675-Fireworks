Fireworks
======

This is the repository for our class project of CS6675

### Section 1: Directory Structure
| Folder Name  | Description |
| ------------- | ------------- |
| Sina_Weibo  | Python version WeiboDataRetrieval. Please refer to README inside.  |
| TermFrequency  | Module for Term Frequency on Weibo label  |
| cs6675_portal | Portal for our projects, including clustering, geo representation, dashboard portal |
| weibo4j_oauth2 | Java version WeiboDataRetrieval. Refer to weibo4j-oauth2 / examples / weibo4j / examples / timeline / GetPublicTimeline.java for data retrieval |

### Section 2: Deploy Guide
#### Prerequisites
In order to use of application, you need to install MongoDB, Node.js and mongojs on your machine first. 
Please refer to the following links for more detailed installation instructions.
- http://docs.mongodb.org/manual/installation/ 
- http://nodejs.org/
- http://howtonode.org/node-js-and-mongodb-getting-started-with-mongojs

#### How to run WeiboDataRetrieval
For Python version, please refer to the README inside directory Sina_Weibo
For Java version, please package the weibo4j_oauth2 project into a jar package like weibo.jar (make sure to select main class weibo4j.examples.timeline.GetPublicTimeline). 
Run the command below to start fetching the Weibo data:
```
java -jar weibo.jar [token_file] [user_interval] [query_interval]
```
- [token_file]: put the access tokens for all the users in the file
- [user_interval]: how long you would delay between each two users
- [query_interval]: how long your would delay between each two queries

#### How to deploy portal
- Install Node.js
- Copy cs6675_portal to your machine
- Change directory to the directory cs6675_portal, then run the command below to start the application server:
```
node index.js
```
- Now, you can visit the following link to view the hottest places around you!
```
http://localhost:3000/map1.html
```

