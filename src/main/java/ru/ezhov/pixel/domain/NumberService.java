package ru.ezhov.pixel.domain;

import java.util.Random;

public class NumberService {
    public int even() {
        while (true) {
            int val = new Random().nextInt(100);
            if(val % 2 == 0){
                return val;
            }
        }
    }

    public int odd() {
        while (true) {
            int val = new Random().nextInt(100);
            if(val % 2 != 0){
                return val;
            }
        }
    }
}
