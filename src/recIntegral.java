// Класс для хранения данных одной строки таблицы
class recIntegral {
    private double lowerBound, upperBound, step, result;

    public recIntegral(double lowerBound, double upperBound, double step, double result) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.step = step;
        this.result = result;
    }

    //Делаем поля защищёнными
    public double getLowerBound() {
        return  lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public double getStep() {
        return step;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    //Интеграл перемещаем сюда
    public double computeIntegral() {
        double start = lowerBound;
        double sumS = 0;
        do {
            double h = Math.min(step, (upperBound - start));
            sumS += h * 0.5 * (Math.cos(start) + Math.cos(start + h));
            start += h;
        } while (start < upperBound);
        return sumS;
    }
}