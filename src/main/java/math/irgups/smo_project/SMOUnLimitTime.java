package math.irgups.smo_project;

/**
 * СМО с неограниченным временем ожидания
 */
public class SMOUnLimitTime extends SMO{

    private final double xi;
    private final int rel;
    private final double abs;
    public SMOUnLimitTime(int k, double in, double out) {
        super(k, in, out);
        xi = ro / k;
        rel = 1;
        abs = in;
    }

    public double getXi() {
        return xi;
    }

    /**
     * Вычисление вероятности простоя системы в предельном стационарном режиме. (4.2.3)
     * @return - вероятность простоя
     */
    public double calculateP0() {
        // Первое слагаемое
        double first = 0;
        for(int i = 0; i < k; i++) {
            first += Math.pow(ro, i) / getFactorial(i);
        }
        double second = (Math.pow(ro, k) / (getFactorial(k) * (1 - xi)));
        System.out.println("ro = " + ro);
        System.out.println("xi = " + xi);
        System.out.println(second);
        return 1 / (first + second);
    }

    /**
     * Получение вероятностей нахождения системы в i состоянии в предельном стационарном режиме. (4.2.1),(4.2.2)
     * @param i - состояние
     * @return вероятность
     */
     public double calculateProbability(int i) {
        if(i == 0)
            return calculateP0();
        // S1 ... Sk
        if(1 <= i && i <= k) {
            return ro/i * calculateProbability(i - 1);
        }
        // Sk+j, Sk+j+1 ...
        else if(i > k) {
            int j = i - k;
            return Math.pow(xi, j) * calculateProbability(k);
        }
        return 0;
     }

    /**
     * Абсолютная пропускная способность (4.2.6)
     * @return A
     */
    public double getAbs() {
        return abs;
    }

    /**
     * Относительная пропускная способность (4.2.5)
     * @return Q
     */
    public int getRel() {
        return rel;
    }

    /**
     * Среднее число занятых каналов. (4.2.7)
     * @return k среднее
     */
    public double calculateBusyChannelsAvg() {
        return ro;
    }

    /**
     * Среднее число заявок в очереди (4.2.8)
     * @return Lоч среднее
     */
    public double calculateLineLengthAvg() {
        return (xi/((1-xi)*(1-xi))) * calculateProbability(k);
    }

    /**
     * Среднее время пребывания заявки в очереди (4.2.9)
     * @return Tож среднее
     */
    public double calculateWaitTimeLineAvg() {
        return calculateLineLengthAvg() / in;
    }

    /**
     * Среднее число заявок в системе (4.2.10)
     * @return Lсист среднее
     */
    public double calculateNumSystemAvg() {
        return calculateBusyChannelsAvg() + calculateLineLengthAvg();
    }

    /**
     * Среднее время пребывания заявки в системе (4.2.11)
     * @return Tсист среднее
     */
    public double calculateTimeSystemAvg() {
        return calculateNumSystemAvg() / in;
    }

    /**
     * Среднее число свободных каналов (4.2.12)
     * @return Kappa среднее
     */
    public double calculateFreeChannelsAvg() {
        return k - ro;
    }

    /**
     * Коэффициент простоя системы (4.2.13)
     * @return k с пр.
     */
    public double calculateKLazy() {
        return calculateFreeChannelsAvg() / k;
    }

    /**
     * Вероятность, того, что в очереди ожидает хотя бы одна заявка (4.2.14)
     * @return P(Lоч > 0)
     */
    public double calculateOneInLine() {
        return (xi / (1 - xi)) * calculateProbability(k);
    }

    /**
     * Вероятность, того, что завяка ожидает обслуживания (4.2.15)
     * @return P(Tож > 0) = P(Lсист >= k)
     */
    public double calculateWaitingProbability() {
        return calculateProbability(k) / (1 - xi);
    }
}
