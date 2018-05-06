import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import scala.Tuple2;

public class SparkKafka {

    public static void main(String[] args) throws Exception {

        final Pattern SPACE = Pattern.compile(" ");

        SparkConf conf = new SparkConf().setAppName("sparkkafka");
        conf.setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaStreamingContext jsc = new JavaStreamingContext(sc, new Duration(2000));

        Collection<String> topics = Arrays.asList("test4");

        HashMap kafkaprops = new HashMap();
        kafkaprops.put("bootstrap.servers", "localhost:9092");
        kafkaprops.put("group.id","xx");
        kafkaprops.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaprops.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        String warLexical= "(.*)attack(.*)|(.*)war(.*)|(.*)bomb(.*)|(.*)kill(.*)|" +
                "(.*)killing(.*)|(.*)attacking(.*)|(.*)bombing(.*)|(.*)weapon(.*)|" +
                "(.*)weapons(.*)|(.*)destroy(.*)|(.*)destroying(.*)|(.*)armed(.*)|" +
                "(.*)arm(.*)|(.*)millitary(.*)|(.*)raping(.*)|(.*)rape(.*)|(.*)dies(.*)" +
                "|(.*)dying(.*)|(.*)defends(.*)|(.*)defense(.*)|(.*)kills(.*)|" +
                "(.*)bombs(.*)|(.*)destruction(.*)|(.*)terror(.*)|" +
                "(.*)terrosism(.*)|(.*)criminal(.*)|(.*)crimes(.*)|(.*)tank(.*)|" +
                "(.*)gun(.*)|(.*)guns(.*)|(.*)revolution(.*)|(.*)airstrikes(.*)|" +
                "(.*)missiles(.*)|(.*)Warplanes(.*)|(.*)launched(.*)|(.*)chemical(.*)|" +
                "(.*)dead(.*)|(.*)fire(.*)";
        String countriesLexical= "(.*)Afghanistan(.*)|(.*)Albania(.*)|(.*)Algeria(.*)|(.*)Andorra(.*)|" +
                "(.*)Angola(.*)|(.*)Antigua and Barbuda(.*)|(.*)Argentina(.*)|" +
                "(.*)Armenia(.*)|(.*)Aruba(.*)|(.*)Australia(.*)|(.*)Austria(.*)|" +
                "(.*)Azerbaijan(.*)|(.*)The Bahamas(.*)|(.*)Bahrain(.*)|" +
                "(.*)Bangladesh(.*)|(.*)Barbados(.*)|(.*)Belarus(.*)|(.*)Belgium(.*)|" +
                "(.*)Belize(.*)|(.*)Benin(.*)|(.*)Bermuda(.*)|(.*)Bhutan(.*)|" +
                "(.*)Bolivia(.*)|(.*)Bosnia and Herzegovina(.*)|(.*)Botswana(.*)|" +
                "(.*)Brazil(.*)|(.*)Brunei(.*)|(.*)Bulgaria(.*)|(.*)Burkina Faso(.*)|" +
                "(.*)Burundi(.*)|(.*)Cambodia(.*)|(.*)Cameroon(.*)|(.*)Canada(.*)|" +
                "(.*)Cape Verde(.*)|(.*)Central African Republic(.*)|(.*)Chad(.*)|" +
                "(.*)Chile(.*)|(.*)People's Republic of China(.*)|" +
                "(.*)Republic of China (Taiwan)(.*)|(.*)Cook Islands(.*)|" +
                "(.*)Colombia(.*)|(.*)Comoros(.*)|(.*)Democratic Republic of the Congo(.*)|" +
                "(.*)Republic of the Congo(.*)|(.*)Costa Rica(.*)|(.*)Côte d'Ivoire(.*)|" +
                "(.*)Croatia(.*)|(.*)Cuba(.*)|(.*)Republic of Cyprus(.*)|" +
                "(.*)Czech Republic(.*)|(.*)Denmark(.*)|(.*)Djibouti(.*)|" +
                "(.*)Dominica(.*)|(.*)Dominican Republic(.*)|" +
                "(.*)Democratic Republic of Congo(.*)|(.*)East Timor(.*)|" +
                "(.*)Ecuador(.*)|(.*)Egypt(.*)|(.*)El Salvador(.*)|" +
                "(.*)Equatorial Guinea(.*)|(.*)Eritrea(.*)|(.*)Estonia(.*)|" +
                "(.*)Ethiopia(.*)|(.*)Fiji(.*)|(.*)Finland(.*)|(.*)France(.*)|" +
                "(.*)Gabon(.*)|(.*)The Gambia(.*)|(.*)Georgia(.*)|(.*)Germany(.*)|" +
                "(.*)Ghana(.*)|(.*)Greece(.*)|(.*)Grenada(.*)|(.*)Guatemala(.*)|" +
                "(.*)Guinea(.*)|(.*)Guinea|Bissau(.*)|(.*)Guyana(.*)|(.*)Haiti(.*)|" +
                "(.*)Holy See (see Vatican City)(.*)|(.*)Honduras(.*)|(.*)Hungary(.*)|" +
                "(.*)Iceland(.*)|(.*)India(.*)|(.*)Indonesia(.*)|(.*)Iran(.*)|" +
                "(.*)Iraq(.*)|(.*)Ireland(.*)|(.*)Israel(.*)|(.*)Italy(.*)|" +
                "(.*)Ivory Coast(.*)|(.*)Jamaica(.*)|(.*)Japan(.*)|(.*)Jordan(.*)|" +
                "(.*)Kazakhstan(.*)|(.*)Kenya(.*)|(.*)Kiribati(.*)|(.*)Kuwait(.*)|" +
                "(.*)Kyrgyzstan(.*)|(.*)Laos(.*)|(.*)Latvia(.*)|(.*)Lebanon(.*)|" +
                "(.*)Lesotho(.*)|(.*)Liberia(.*)|(.*)Libya(.*)|(.*)Liechtenstein(.*)|" +
                "(.*)Lithuania(.*)|(.*)Luxembourg(.*)|(.*)Republic of Macedonia(.*)|" +
                "(.*)Madagascar(.*)|(.*)Malawi(.*)|(.*)Malaysia(.*)|(.*)Maldives(.*)|" +
                "(.*)Mali(.*)|(.*)Malta(.*)|(.*)Marshall Islands(.*)|(.*)Mauritania(.*)|" +
                "(.*)Mauritius(.*)|(.*)Mexico(.*)|(.*)Federated States of Micronesia(.*)|" +
                "(.*)Moldova(.*)|(.*)Monaco(.*)|(.*)Mongolia(.*)|(.*)Montenegro(.*)|" +
                "(.*)Morocco(.*)|(.*)Mozambique(.*)|(.*)Myanmar(.*)|(.*)Burma(.*)|" +
                "(.*)Namibia(.*)|(.*)Nauru(.*)|(.*)Nepal(.*)|(.*)Netherlands(.*)|" +
                "(.*)New Zealand (Aotearoa)(.*)|(.*)Nicaragua(.*)|(.*)Niger(.*)|" +
                "(.*)Nigeria(.*)|(.*)Niue(.*)|(.*)North Korea(.*)|(.*)Norway(.*)|" +
                "(.*)Oman(.*)|(.*)Pakistan(.*)|(.*)Palau(.*)|(.*)Panama(.*)|" +
                "(.*)Papua New Guinea(.*)|(.*)Paraguay(.*)|(.*)Peru(.*)|" +
                "(.*)Philippines(.*)|(.*)Poland(.*)|(.*)Portugal(.*)|(.*)Qatar(.*)|" +
                "(.*)Romania(.*)|(.*)Russia(.*)|(.*)Rwanda(.*)|" +
                "(.*)Saint Kitts and Nevis(.*)|(.*)Saint Lucia(.*)|" +
                "(.*)Saint Vincent and the Grenadines(.*)|(.*)Samoa(.*)|" +
                "(.*)San Marino(.*)|(.*)São Tomé and Príncipe(.*)|(.*)Saudi Arabia(.*)|" +
                "(.*)Senegal(.*)|(.*)Serbia(.*)|(.*)Seychelles(.*)|(.*)Sierra Leone(.*)|" +
                "(.*)Singapore(.*)|(.*)Slovakia(.*)|(.*)Slovenia(.*)|" +
                "(.*)Solomon Islands(.*)|(.*)Somalia(.*)|(.*)South Africa(.*)|" +
                "(.*)South Korea(.*)|(.*)South Sudan(.*)|(.*)Spain(.*)|" +
                "(.*)Sri Lanka(.*)|(.*)Sudan(.*)|(.*)Suriname(.*)|(.*)Swaziland(.*)|" +
                "(.*)Sweden(.*)|(.*)Switzerland(.*)|(.*)Syria(.*)|" +
                "(.*)Taiwan(.*)|(.*)Tajikistan(.*)|(.*)Tanzania(.*)|(.*)Thailand(.*)|" +
                "(.*)Togo(.*)|(.*)Tonga(.*)|(.*)Trinidad and Tobago(.*)|" +
                "(.*)Tunisia(.*)|(.*)Turkey(.*)|(.*)Turkmenistan(.*)|(.*)Tuvalu(.*)|" +
                "(.*)Uganda(.*)|(.*)Ukraine(.*)|(.*)United Arab Emirates(.*)|" +
                "(.*)United Kingdom(.*)|(.*)United States(.*)|(.*)Uruguay(.*)|" +
                "(.*)Uzbekistan(.*)|(.*)Vanuatu(.*)|(.*)Vatican City(.*)|" +
                "(.*)Venezuela(.*)|(.*)Vietnam(.*)|(.*)Yemen(.*)|(.*)Zambia(.*)|" +
                "(.*)Zimbabwe(.*)";



        JavaInputDStream<ConsumerRecord<Integer, String>> ds = KafkaUtils.createDirectStream(
                jsc, LocationStrategies.PreferConsistent(),
                ConsumerStrategies.<String, String>Subscribe(topics, kafkaprops));

        JavaDStream<String> lines = ds.map(x -> x.value());

        JavaDStream<String> filtredLines = lines.filter((org.apache.spark.api.java.function.Function<String, Boolean>)
                line -> line.matches(warLexical));

        JavaDStream<String> words = filtredLines.flatMap(x -> Arrays.asList(SPACE.split(x)).iterator());

        JavaDStream<String> filtredWords = words.filter((org.apache.spark.api.java.function.Function<String, Boolean>)
                word -> word.matches(countriesLexical));



        filtredWords.foreachRDD(rdd -> {
            rdd.foreachPartition(partitionOfRecords -> {

                Producer<Integer, String> producer = MyKafkaProducer.getProducer();
                while (partitionOfRecords.hasNext()) {
                    producer.send(new ProducerRecord<>("test", 1, partitionOfRecords.next()));
                }

            });
        });

        jsc.start();
        jsc.awaitTermination();
    }

}