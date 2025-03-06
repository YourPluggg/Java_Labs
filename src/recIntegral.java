// Класс для хранения данных одной строки таблицы
class recIntegral {
    double lowerBound, upperBound, step, result;

    public recIntegral(double lowerBound, double upperBound, double step, double result) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.step = step;
        this.result = result;
    }
}