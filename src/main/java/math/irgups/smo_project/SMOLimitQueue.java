package math.irgups.smo_project;

import java.util.ArrayList;

/**
 * СМО с ограниченной очередью
 */
public class SMOLimitQueue extends SMO {

    /**
     * Длина очереди
     */
    private final int queueLen;
    /**
     * Коэффициент использования системы
     */
    private final double xi;
    public SMOLimitQueue(int k, double in, double out, int queueLen) {
        super(k, in, out);
        this.queueLen = queueLen;
        xi = ro / k;
    }

    public double getXi() {
        return xi;
    }

    /**
     * Вероятность простоя системы в предельном стационарном режиме (4.1.3)
     * @return P0
     */
    public double calculateP0() {
        double first = 0;
        for(int i = 0; i<=k; i++) {
            first += Math.pow(ro, i) / getFactorial(i);
        }
        double second = 0;
        second = (Math.pow(ro, k) / getFactorial(k)) * ((xi * (1 - Math.pow(xi, queueLen))) / (1-xi));
        return 1 / (first + second);
    }

    /**
     * Получение вероятностей нахождения системы в i состоянии в предельном стационарном режиме. (4.1.1),(4.1.2)
     * @param i - состояние
     * @return вероятность
     */
    public double calculateProbability(int i) {
        if(i == 0)
            return calculateP0();
        if(1 <= i && i <= k)
            return (Math.pow(ro, i) / getFactorial(i)) * calculateP0();
        else if(i > k) {
            int j = i - k;
            return Math.pow(xi, j) * (Math.pow(ro, k) / getFactorial(k)) * calculateP0();
        }
        return 0;
    }

    /**
     * Вероятность отказа (4.1.4)
     * @return Pотк
     */
    public double calculateNoProbability() {
        return Math.pow(xi, queueLen) * calculateProbability(k);
    }

    /**
     * Относительная пропускная способность (4.1.5)
     * @return Q
     */
    public double calculateRel() {
        return 1 - calculateNoProbability();
    }

    /**
     * Абсолютная пропускная способность  (4.1.6)
     * @return A
     */
    public double calculateAbs() {
        return in * calculateRel();
    }

    /**
     * Среднее число занятых каналов (4.1.7)
     * @return K среднее
     */
    public double calculateBusyChannelsAvg() {
        return ro * calculateRel();
    }

    /**
     * Среднее число заявок в очереди (4.1.8)
     * @return Lоч среднее
     */
    public double calculateLineLengthAvg() {
        return ((ro / k) * calculateProbability(k)) * (
                (1 - (queueLen + 1) * Math.pow(xi, queueLen) + queueLen * Math.pow(xi, queueLen + 1))
                        /
                        (  (1 - xi) * (1 - xi)  )
                );
    }

    /**
     * Среднее число заявок в системе (4.1.9)
     * @return Lсист среднее
     */
    public double calculateNumSystemAvg() {
        return calculateBusyChannelsAvg() + calculateLineLengthAvg();
    }

    /**
     * Среднее время пребывания заявки в очереди (4.1.12)
     * @return Tож среднее
     */
    public double calculateWaitTimeLineAvg() {
        return calculateLineLengthAvg() / in;
    }

    /**
     * Среднее время обслуживания (4.1.13)
     * @return Tобсл среднее
     */
    public double calculateTimeAvg() {
        return calculateBusyChannelsAvg() / in;
    }

    /**
     * Среднее время пребывания заявки в системе (4.1.15)
     * @return Tсист среднее
     */
    public double calculateTimeSystemAvg() {
        return calculateNumSystemAvg() / in;
    }

    /**
     * Коэф. простоя заявки (4.1.16)
     * @return k з пр.
     */
    public double calculateKLazy() {
        return calculateLineLengthAvg() / queueLen;
    }

}
