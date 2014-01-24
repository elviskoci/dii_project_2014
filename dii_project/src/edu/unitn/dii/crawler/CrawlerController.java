package edu.unitn.dii.crawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class CrawlerController {

        public static void main(String[] args) throws Exception {
                
        		/* Command Line Code
        		if (args.length < 3) {
        		 
                        System.out.println("Needed parameters: ");
                        System.out.println("\t rootFolder (it will contain intermediate crawl data)");
                        System.out.println("\t numberOfCralwers (number of concurrent threads)");
                        System.out.println("\t storageFolder (a folder for storing downloaded images)");
                        return;
                }
                
                String rootFolder = args[0];
                int numberOfCrawlers = Integer.parseInt(args[1]);
                String storageFolder = args[2];*/

	        	String rootFolder = "./crawler_data";
	            int numberOfCrawlers = 2;
	            //downloaded files stored here
	            String storageFolder = "./storage";
	            
                CrawlConfig config = new CrawlConfig();

                // intermidiate crawl data is stored here
                config.setCrawlStorageFolder(rootFolder);

        		// 1 request per second (1000 milliseconds between requests).
        		config.setPolitenessDelay(1000);
        		
        		// crawl depth
        		config.setMaxDepthOfCrawling(2);
        		
        		// if true resumes the crawl from a previously interrupted/crashed crawl
        		config.setResumableCrawling(false);
        		
                String[] crawlDomains = new String[] { "http://dati.trentino.it/dataset/prodotti-tradizionali-trentini" , "http://dati.trentino.it/dataset/ricette-tipiche-trentine" };

                PageFetcher pageFetcher = new PageFetcher(config);
                RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
                RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
                CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
                for (String domain : crawlDomains) {
                        controller.addSeed(domain);
                }

                OpenDataCrawler.configure(crawlDomains, storageFolder);

                controller.start(OpenDataCrawler.class, numberOfCrawlers);
        }

}

