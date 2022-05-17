package math.irgups.smo_project;

import java.util.ArrayList;

/**
 * СМО с ограниченным временем ожидания
 */
public class SMOLimitTime extends SMO{
    /**
     * Точность
     */
    private final int eps;
    /**
     * Интенсивность ухода заявок из очереди (ν)
     */
    private final double v;
    /**
     *  ν / µ
     */
    private final double fi;

    public int getEps() {
        return eps;
    }

    public double getV() {
        return v;
    }

    public double getFi() {
        return fi;
    }

    public SMOLimitTime(int k, double in, double out, double v, int eps) {
        super(k, in, out);
        this.v = v;
        this.fi = v / this.out;
        this.eps = eps;
    }

    /**
     * Расчет вероятности простоя СМО в предельном стационарном режиме (P0)
     * @return - p0
     */
    private double calculateP0() {
        // Первое слагаемое формулы (4.3.3)
        double first_sum = 0;
        // Второе слагаемое формулы (4.3.3)
        double second_sum = 0;

        double mul = 1;
        int i = 0;
        int j = 1;
        int m = 1;

        // Расчет первого слагаемого
        while(i <= k) {
            first_sum += (Math.pow(ro, i)) / getFactorial(i);
            i++;
        }

        // Второе слагаемое
        double denom = 1;
        while(j <= eps) {
            // Числитель во втором слагаемом
            double numer = Math.pow(ro, j);
            // Знаменатель во втором слагаемом; m - число мест в очереди.
            while(m <= j) {
                denom *= (k + m * fi);
                m++;
            }
            second_sum += numer / denom;
            j++;
        }

        // Складываем первое и второе слагаемое в формуле (4.3.3)
        second_sum = second_sum * (Math.pow(ro, k) / getFactorial(k));

        // Формула (4.3.3)
        return Math.pow(first_sum + second_sum, -1);
    }

    /**
     * Получение вероятностей нахождения системы в i состоянии в предельном стационарном режиме
     * @return double, вероятность нахождения системы в состоянии Si/Sk+j
     */
    public double calculateProbability(int i) {
        // S0
        if (i == 0)
            return calculateP0();
        // S1 ... Sk
        if (1 <= i && i <= k) {
            return (in / (i * out)) * calculateProbability(i - 1);
            // Sk+j, Sk+j+1 ... Sn
        }
        else if (i > k) {
            int j = i - k;
            return (in / (k * out + j * v)) * calculateProbability(i - 1);
        }
        return 0;
    }

    /**
     * Получение вероятностей занятости канала Pi; i = 1..k
     * @return распределение вероятностей
     */
    public ArrayList<Double> calculateBusyChannelsSpread() {
        ArrayList<Double> spread = new ArrayList<>(k + 1);
        double p0 = calculateP0();
        spread.add(0, p0);
        double sum = p0;
        for (int i = 1; i < k; i++) {
            double temp = calculateProbability(i);
            sum += temp;
            spread.add(i, temp);
        }
        spread.add(1 - sum);
        return spread;
    }

    /**
     * @param spread - распределение вероятностей числа занятых каналов
     * @return - среднее число занятых каналов
     */
    public double calculateAverageBusyChannels(ArrayList<Double> spread) {
        double result = 0;
        for(int i = 0; i < spread.size(); i++) {
            result += i * spread.get(i);
        }
        return result;
    }

    /**
     * Абсолютная пропускная способность(среднее число завявок, обслуженных в единицу времени)
     * @return A - абс. пропускная способность
     */
    public double getAbs() {
        return in - v * calculateLineLengthAvg();
    }

    /**
     * Относительная пропускная способность(вероятность того, что заявка будет обслужена)
     * @return Q - отн. пропускная способность
     */
    public double getRel() {
        return getAbs() / in;
    }

    /**
     * Расчет среднего числа заявок в очереди
     * @return - Lоч ср.
     */
    public double calculateLineLengthAvg() {
        return (ro - calculateAverageBusyChannels(calculateBusyChannelsSpread())) / fi;
    }



    /**
     * Среднее число завяок, ушедших из очереди
     * @return - Lух.
     */
    public double getNumExit() {
        return v * calculateLineLengthAvg();
    }

}
