package com.lab8;
import java.util.LinkedList;
 /**URLPool хранит список
         всех URL-адресов для поиска**/
public class URLPool {
     /** Два списка: один для всех сайтов, рассмотренных до текущего момента, и один,
      который включает только необработанные сайты **/
     LinkedList<URLDepthPair> findLink;
     LinkedList<URLDepthPair> viewedLink;
    int maxDepth;
    int cWait;
/**поиск максимальной глубины поиска**/
    public URLPool(int maxDepth) {
        this.maxDepth = maxDepth;
        findLink = new LinkedList<URLDepthPair>();
        viewedLink = new LinkedList<URLDepthPair>();
        cWait = 0;
    }

     /**
      *Необходимо сохранить URL-адреса и их глубину
      * поиска вместе,
      *  как экземпляры класса с именем URLDepthPair
      */
     public synchronized URLDepthPair getPair() {
        while (findLink.size() == 0) {
            cWait++;
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Ignoring InterruptedException");
            }
            cWait--;
        }
        URLDepthPair nextPair = findLink.removeFirst();
        return nextPair;
    }
/** способ добавления пары URL-глубина к пулу**/
    public synchronized void addPair(URLDepthPair pair) {
        if(URLDepthPair.check(viewedLink,pair)) {
            viewedLink.add(pair);
            if (pair.getDepth() < maxDepth) {
                findLink.add(pair);
                notify();
            }
        }
    }

    public synchronized int getWait() {
        return cWait;
    }

    public LinkedList<URLDepthPair> getResult() {
        return viewedLink;
    }

}