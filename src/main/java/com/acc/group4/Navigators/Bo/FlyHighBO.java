package com.acc.group4.Navigators.Bo;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.io.*;
import java.time.Duration;
import java.util.*;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


import java.util.logging.Logger;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import com.acc.group4.Navigators.UIRepresentation.FlightDetailsTable;
import com.acc.group4.Navigators.Utils.Constants;
import com.acc.group4.Navigators.Utils.FlyHighLogger;
import com.acc.group4.Navigators.Utils.PatternRecognition;
//import com.acc.group4.Navigators.Utils.PatternRecognization;
import com.acc.group4.Navigators.WordCompletion.AirportCodeDictionary;
import com.acc.group4.Navigators.WordCompletion.spell_check;
import com.acc.group4.Navigators.WordCompletion.word_completion;
import com.acc.group4.Navigators.crawler.WebCrwaler;
import com.acc.group4.Navigators.pojo.FlightDetails;


public class FlyHighBO {
        private static FlyHighBO flyHighInstance = null;
        private WebCrwaler webCrawler = WebCrwaler.getInstance();
        private static Logger logger ;  
        public static FlyHighBO getInstance() {
                flyHighInstance = flyHighInstance == null ? new FlyHighBO() : flyHighInstance;
                return flyHighInstance;
        }
  
        private static Scanner sc = new Scanner(System.in);


        public void getFlightDetails() {
            System.out.println(">>>Entering getFlightDetails method<<<");
            try {
                getFlightDetailsOperation();
            } catch (Exception e) {
                e.printStackTrace();
                // Handle the exception according to your application's needs
            }
            System.out.println(">>>Exiting getFlightDetails method<<<");
        }
        
        public static String insert_source1(){
                System.out.println("\nEnter the source: ");
                String source = sc.nextLine();
                String cityName = "none";
                //String airportCode = "none";
                boolean flag = true;
                
                spell_check sp = new spell_check();
                sp.spellChecking(source);
                
                // Call the word_completion method
        word_completion wordCompletionInstance = new word_completion();
        // Convert the first letter of each word to uppercase
        String filePath = "city_names.txt";
                List<String> words = wordCompletionInstance.navigators_readWordsFromFile(filePath);
                //System.out.println(words);
                if (words != null) {
                        wordCompletionInstance.navigators_Trie(words);
                        String camelCaseInput = wordCompletionInstance.navToCamelCaseGiles(source);
                        //System.out.println("camelCaseInput" + camelCaseInput);
                        List<String> suggestion = wordCompletionInstance.navSuggestGiles(camelCaseInput);
                        if(suggestion.size()!=0) {
                                System.out.println("Suggestions for Word Completion : ");
                                for (int i = 0; i < suggestion.size(); i++) {
                            System.out.println((i + 1) + ": " + suggestion.get(i));
                                }
                                while(flag) {
                                        System.out.println("Choose the city from above:");
                                        int selectedIndex = sc.nextInt();
                                        //System.out.println("selectedIndex: " + selectedIndex);
                                        cityName = wordCompletionInstance.navigators_completedWord(selectedIndex,suggestion);
                                        
                                        if(cityName != "none") {
                                                flag = false;
                                        }
                                } 
                                //return cityName;
                                AirportCodeDictionary airportCodeDict = new AirportCodeDictionary();
                                String airportCode = airportCodeDict.getAPCodeGiles(cityName);
                                //System.out.println("AirportCode is " + airportCode);
                                return airportCode;
                        }
                        else {
                                System.out.println("Invalid input. Please enter the source again");
                                insert_source1();
                                return "none";
                        }
                } else {
                        System.out.println("Failed to read words from the file.");
                        insert_source1();
                        return "none";
                }                
                }
        
        public static String insert_dest() {
            System.out.println("Enter the Destination: ");
            Scanner sc1 = new Scanner(System.in);
            String dest = sc1.nextLine();
            String cityName1 = "none";
            boolean flag = true;
            
            spell_check sp = new spell_check();
            sp.spellChecking(dest);
            

            // Call the word_completion method
            //System.out.println("Failed to read words fr");
            word_completion wordCompletionInstance1 = new word_completion();


            if (!dest.isEmpty()) {
                // Convert the first letter of each word to uppercase
                String filePath = "city_names.txt";
                List<String> words = wordCompletionInstance1.navigators_readWordsFromFile(filePath);


                if (words != null) {
                    wordCompletionInstance1.navigators_Trie(words);
                    String camelCaseInput = wordCompletionInstance1.navToCamelCaseGiles(dest);


                    List<String> suggestion = word_completion.navSuggestGiles(camelCaseInput);
                    if (suggestion.size() != 0) {
                        System.out.println("Suggestions: ");
                        for (int i = 0; i < suggestion.size(); i++) {
                            System.out.println((i + 1) + ": " + suggestion.get(i));
                        }
                        while (flag) {
                            System.out.println("CHOOSE THE OPTION FROM ABOVE");
                            int selectedIndex = sc.nextInt();
                            System.out.println("selectedIndex: " + selectedIndex);
                            cityName1 = wordCompletionInstance1.navigators_completedWord(selectedIndex, suggestion);


                            if (!"none".equals(cityName1)) {
                                flag = false;
                            }
                        }
                        AirportCodeDictionary airportCodeDict = new AirportCodeDictionary();
                                        String airportCode = airportCodeDict.getAPCodeGiles(cityName1);
                                        System.out.println("AirportCode is " + airportCode);
                                        return airportCode;
                    } else {
                        System.out.println("Invalid input. Please enter the Destination again");
                        insert_dest();
                        return "none";
                    }
                } else {
                    System.out.println("Failed to read words from the file.");
                    insert_dest();
                    return "none";
                }
            } else {
                System.out.println("Invalid input. Destination cannot be empty.");
                return "none";
            }
        }


        
        private void getFlightDetailsOperation() {
                
                try {
                System.out.println(">>>Entering getFlightDetailsOperation method<<<");
                String source = insert_source1();
                String destination = insert_dest();
                
                PatternRecognition datePattern = new PatternRecognition();
                String date = datePattern.checkDateFormat();
//                System.out.println("Enter the Number of travellers: ");
//                String numberOfPeople = sc.next();
                ArrayList<FlightDetails> flightDetails = new ArrayList<FlightDetails>();
//                System.out.println("How many url's to be crawled :: ");
//                int numberOfURLSToCrawl = sc.nextInt();
//                String Url = Constants.KAYAK_INITIAL_URL_BEFORE_PARAM+source+"-"+destination+"/" + date;
//                String url2 = Constants.CHEAPFLIGHT_INITIAL_URL_BEFORE_PARAM+source+"-"+destination+"/" + date;
                String url1 = "https://www.google.com/travel/flights?q=Flights%20to%20"+ destination +"%20from%20"+ source +"%20on%20"+ date +"%20with%201%20adult%20first%20class%20one%20way&curr=USD";
                String Kayak_Url = Constants.KAYAK_INITIAL_URL_BEFORE_PARAM+source+"-"+destination+"/" + date;
//                HashSet<String> kayak_urlForFlightPages = webCrawler.crawlFlightSites(Kayak_Url, numberOfURLSToCrawl);
                try {
                        //String Url = Constants.KAYAK_INITIAL_URL_BEFORE_PARAM + date + Constants.KAYAK_INITIAL_URL_AFTER_PARAM;
                        Document doc = Jsoup.parse(webCrawler.WebScrapper(url1));
                        Elements flightDethtml = doc.getElementsByClass(Constants.G_FLIGHT_DETAIL_ROOT);
                        //System.out.println("Test1 :: "+flightDethtml);
                        for (Element element : flightDethtml) {
                                FlightDetails flightInfo = new FlightDetails();
                                flightInfo.setFlightPrice(element.getElementsByClass(Constants.G_FLIGHT_PRICE_HTML_CLASS).text());
                                flightInfo.setTimeToReach(
                                                element.getElementsByClass(Constants.G_FLIGHT_TIME_HTML_CLASS).textNodes().get(0).text());
                                flightInfo.setFlightName(element.selectXpath("//div[@class='hF6lYb sSHqwe ogfYpf tPgKwe']/span[@class='h1fkLb']/span[1]").text());
                                flightInfo.setStops(element.getElementsByClass(Constants.G_FLIGHT_STOPS_HTML_CLASS).text());
                                flightDetails.add(flightInfo);
                        }
                        System.out.println("Flights :: " + flightDetails);
                        FlightDetailsTable.showFlights(flightDetails);
                        System.out.println("Cheapest Flight on Google: Price "+
                        doc.getElementsByClass("YMlIz FpEdX jLMuyc").text());
                        writeHashMapToCSV(flightDetails,source, destination, "google_flight_data.csv");
                }
                catch (Exception e) {
                        e.printStackTrace();
                }
                try {
                        //String Url = Constants.KAYAK_INITIAL_URL_BEFORE_PARAM + date + Constants.KAYAK_INITIAL_URL_AFTER_PARAM;
                        Document doc = Jsoup.parse(webCrawler.WebScrapper(Kayak_Url));
                        Elements flightDethtml = doc.getElementsByClass(Constants.KAYAK_FLIGHT_DETAIL_ROOT);
                        //System.out.println("Test1 :: "+flightDethtml);
                        for (Element element : flightDethtml) {
                                FlightDetails flightInfo = new FlightDetails();
                                flightInfo.setFlightPrice(element.getElementsByClass(Constants.KAYAK_FLIGHT_PRICE_HTML_CLASS).text());
                                flightInfo.setTimeToReach(
                                                element.getElementsByClass(Constants.KAYAK_FLIGHT_TIME_HTML_CLASS).textNodes().get(0).text());
                                flightInfo.setFlightName(
                                                element.getElementsByClass(Constants.KAYAK_FLIGHT_OPERATOR_HTML_CLASS).textNodes().get(0).text());
                                flightInfo.setStops(element.getElementsByClass(Constants.KAYAK_FLIGHT_STOPS_HTML_CLASS).text());
                                flightDetails.add(flightInfo);
                        }
                        System.out.println("Flights :: " + flightDetails);
                        FlightDetailsTable.showFlights(flightDetails);
                        System.out.println("Cheapest Flight on KAYAK : Price "+
                        doc.getElementsByClass("Hv20-value").text());
                        writeHashMapToCSV(flightDetails,source, destination, "kayak_flight_data.csv");
                }
                catch (Exception e) {
                        e.printStackTrace();
                }
                }catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid option.");
                    sc.nextLine(); // Clear the buffer
                } catch (Exception e) {
                    System.out.println("An error occurred: " + e.getMessage());
                    e.printStackTrace();
                }
                System.out.println(">>>Exiting getFlightDetailsOperation method<<<");
               
        }
      
        private static String cleanString(String input) {
                // Remove leading and trailing whitespaces
                String cleanedString = input.trim();
        
                // Replace consecutive whitespaces with a single space
                cleanedString = cleanedString.replaceAll("\\s+", " ");
        
                // Remove unwanted characters and spaces
                cleanedString = cleanedString.replaceAll("[,$\\s]", "")
                                                                   .replace("h", "")
                                                                   .replace("m", "");
        
                return cleanedString;
        }
        
        private static void writeHashMapToCSV(ArrayList<FlightDetails> flightDetails,String source,String destination, String csvFilePath)
    {
        try (BufferedWriter csvWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFilePath), "UTF-8")))
        {
            // Write CSV header
            csvWriter.append("From,Destination,Airline,Price,Time,Stops\n");


                        for (FlightDetails flightDetail : flightDetails) {
                                String From = cleanString(source);
                                String Destination = cleanString(destination);
                                String flightName = cleanString(flightDetail.getFlightName());
                                String Price = cleanString(flightDetail.getFlightPrice());
                                String Time = (flightDetail.getTimeToReach());
                                String Stops = cleanString(flightDetail.getStops());


                // Append the data to the CSV file
                csvWriter.append(String.join(",", From, Destination, flightName,Price+"$", Time, Stops));
                csvWriter.append("\n");


                                System.out.println("File written successfully: " + csvFilePath);
            }
}
                catch (IOException e)
                {
                        e.printStackTrace();
                }
        }
}