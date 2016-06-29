# spring-morphia-db-poc
Multi-tenancy on the database level: One way to separate data for multiple clients is to have individual databases per tenant.
By default, Morphia doesn't provice multi-tenancy.
In this project, we have implemented multi-tenancy in Morphai Using Spring,

How to implement multi-tenancy in mongo db using morphia ? 

Ans: Suppose you are working on webservice or web application.

     1. A client will send tenant id as header parameter.
     
     2. Write one filter and get the tenant details from the Cache by tenant id.
     
     3. Set the schema name in the filter 
        for example:    
        MDC.put(Constants.MONGO_TENANT_DB, "uat");
        
     4. MongoMultiTenancyInterceptor will get called before any DB operation/orm call.
        String tenantDBName = MDC.get(Constants.MONGO_TENANT_DB);
        
			  LOG.info("Switching to database:  "+tenantDBName);
			  if(StringUtils.isNotBlank(tenantDBName)){
				      MultitenantDatastoreFactory.setDatabaseNameForCurrentThread(tenantDBName);
			  }
			  Setting the Database name for current Thread.
      
     5. MultitenantDatastoreFactory : Create datastore instance per tenant schema and store into HashMap.   
     6. MultitenantDatastoreFactory.getDS(): Returns the datastore from the Hashmap by database name
     
     7. UserRepository: It will get Datastore object from the factory method and perform any DB operation.
     
     
     # Mong DB Bulk API[Performance Testing]-- https://docs.mongodb.com/manual/reference/method/db.collection.insertMany/
     
     public void insertMany(List<User> users) {
		MongoClient mongoClient=dsFactory.getDS().getMongo();
		MongoDatabase database = mongoClient.getDatabase("vikash_mongo");
        MongoCollection<Document> collection = database.getCollection("users");
        List<Document>docs=new ArrayList<Document>();
        for(User user:users){
        	Document doc=new Document();
        	doc.put("ic" , user.getIc());
        	doc.put("name" , user.getName());
        	doc.put("display_name" , user.getDisplayName());
        	doc.put("age" , user.getAge());
        	doc.put("gender" , "MALE");
        	doc.put("creationDate" , new Date());
        	doc.put("lastChange" , new Date());
        	docs.add(doc);
        }
        InsertManyOptions options= new InsertManyOptions();
        collection.insertMany(docs, options.ordered(true));
	}
 
