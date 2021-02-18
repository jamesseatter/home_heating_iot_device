package eu.seatter.homemeasurement.collector.services.database;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import eu.seatter.homemeasurement.collector.model.Measurement;
import org.bson.UuidRepresentation;
import org.bson.codecs.UuidCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * Created by IntelliJ IDEA.
 * User: jas
 * Date: 13/02/2021
 * Time: 17:33
 */
@Service
public class MongoDB {

    private MongoClient mongoClient;
    private String dbName = "homemeasurement";
    private MongoDatabase database;
    private CodecRegistry pojoCodecRegistry;
    private String measurementCollectionName = "measurements";

    public MongoDB() {
        String dbServer = "r2d2";
        String userName = "hm_mp"; // the userName name
        char[] userPassword = "hm_mp".toCharArray(); // the password as a character array

        MongoCredential credential = MongoCredential.createCredential(userName, dbName, userPassword);

        mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .credential(credential)
                        .applyToClusterSettings(builder ->
                                builder.hosts(Arrays.asList(new ServerAddress(dbServer, 27017))))
                        .build()
        );

        database = mongoClient.getDatabase(dbName);

        //Access the collection
        pojoCodecRegistry = fromRegistries(CodecRegistries.fromCodecs(new UuidCodec(UuidRepresentation.JAVA_LEGACY)),
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
    }

    public boolean addEntry(Measurement measurement) {
        MongoCollection<Measurement> collection = database.getCollection(measurementCollectionName, Measurement.class);
        collection = collection.withCodecRegistry(pojoCodecRegistry);

        //insert the measurement
        final InsertOneResult insertOneResult = collection.insertOne(measurement);

        return insertOneResult.wasAcknowledged();
    }

    public List<Measurement> getAll() {

        MongoCollection<Measurement> collection = database.getCollection(measurementCollectionName, Measurement.class);
        collection = collection.withCodecRegistry(pojoCodecRegistry);

        List<Measurement> measurements = collection.find().into(new ArrayList<Measurement>());

        return measurements;
//        return collection.find().into(new ArrayList<Measurement>());
    }

    public boolean removeById(String id) {
        MongoCollection<Measurement> collection = database.getCollection(measurementCollectionName, Measurement.class);
        collection = collection.withCodecRegistry(pojoCodecRegistry);

        return collection.deleteOne(eq("id", id)).wasAcknowledged();
    }
}
