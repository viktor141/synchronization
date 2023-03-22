package org.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {

        ThreadGroup threadGroup = new ThreadGroup("PathGenerators");

        for (int i = 0; i < 1000; i++) {

            new Thread(threadGroup, ()-> {
                String path = generateRoute("RLRFR", 100);
                int count = 0;
                for (char c: path.toCharArray()) {
                    if(c == 'R'){
                        count++;
                    }
                }

                putToMap(count);
            }).start();
        }

        while (threadGroup.activeCount() != 0){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        int max = 0, keyOfMax = 0;
        for (Map.Entry<Integer,Integer> entry :sizeToFreq.entrySet()) {
            if(entry.getValue() > max){
                max = entry.getValue();
                keyOfMax = entry.getKey();
            }
        }

        System.out.printf("Самое частое количество повторений %d (встретилось %d раз)\n", keyOfMax, max);
        System.out.println("Другие размеры: ");

        for (Map.Entry<Integer, Integer> entry: sizeToFreq.entrySet()) {
            if(entry.getKey() != keyOfMax){
                System.out.printf("%d (%d раз)\n", entry.getKey(), entry.getValue() );
            }
        }

        //Самое частое количество повторений 61 (встретилось 9 раз)
        //Другие размеры:
        //- 60 (5 раз)
        //- 64 (3 раз)
        //- 62 (6 раз)
        //...

    }


    public static void putToMap(int count){
        synchronized (sizeToFreq) {
            if (sizeToFreq.containsKey(count)) {
                sizeToFreq.put(count, sizeToFreq.get(count) + 1);
            } else {
                sizeToFreq.put(count, 1);
            }
        }
    }


    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}