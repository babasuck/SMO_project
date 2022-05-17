package math.irgups.smo_project;

import java.lang.Math;
import java.util.ArrayList;

/**
 * Абстрактная модель SMO, необходимая для реализации всех остальных
 */
public abstract class SMO {
    /**
     * Число каналов обслуживания
     */
    protected int k;
    /**
     * Интенсивность входящего потока (λ)
     */
    protected double in;
    /**
     * Интенсивность обслуживания одним каналом(интенсивность выхода) (µ)
     */
    protected double out;
    /**
     * Коэффициент загрузки канала (ρ)
     */
    protected double ro;
    /**
     * Состояния каналов системы
     */
    protected ArrayList<Double> channels;
    public SMO(int k, double in, double out) {
        this.k = k;
        this.in = in;
        this.out = out;
        this.ro = in / out;
        this.channels = new ArrayList<>(k + 1);
    }


    public int getK() {
        return k;
    }

    public double getIn() {
        return in;
    }

    public double getOut() {
        return out;
    }

    public double getRo() {
        return ro;
    }

    public ArrayList<Double> getChannels() {
        return channels;
    }

    /**
     * Получить факториал числа
     * @param n - число
     * @return - результат (n!)
     */
    protected int getFactorial(int n) {
        if (n <= 1) {
            return 1;
        }
        else {
            return n * getFactorial(n - 1);
        }
    }

}