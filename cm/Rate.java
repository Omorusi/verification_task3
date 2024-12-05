package cm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Rate {
    private CarParkKind kind;
    private BigDecimal hourlyNormalRate;
    private BigDecimal hourlyReducedRate;
    private ArrayList<Period> reduced = new ArrayList<>();
    private ArrayList<Period> normal = new ArrayList<>();

    public Rate(CarParkKind kind, ArrayList<Period> reducedPeriods, ArrayList<Period> normalPeriods, BigDecimal normalRate, BigDecimal reducedRate) {
        if (reducedPeriods == null || normalPeriods == null) {
            throw new IllegalArgumentException("periods cannot be null");
        }
        if (normalRate == null || reducedRate == null) {
            throw new IllegalArgumentException("The rates cannot be null");
        }
        if (normalRate.compareTo(BigDecimal.ZERO) < 0 || reducedRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("A rate cannot be negative");
        }
        if (normalRate.compareTo(reducedRate) < 0) {
            throw new IllegalArgumentException("The normal needs to be bigger or equal to the reduced rate");
        }
        if (!isValidPeriods(reducedPeriods) || !isValidPeriods(normalPeriods)) {
            throw new IllegalArgumentException("The periods are not valid individually");
        }
        if (!isValidPeriods(reducedPeriods, normalPeriods)) {
            throw new IllegalArgumentException("The periods overlaps");
        }
        if(normalRate.compareTo(BigDecimal.ZERO) < 0  || normalRate.compareTo(BigDecimal.valueOf(10) ) > 0)
        {
            throw  new IllegalArgumentException("The normal rate cant be bigger that 10");
        }
       
        if(reducedRate.compareTo(BigDecimal.ZERO) < 0  || reducedRate.compareTo(BigDecimal.valueOf(10) ) > 0)
        {
            throw  new IllegalArgumentException("The reduce rate cant be bigger that 10");
        }
       
      
        this.kind = kind;
        this.hourlyNormalRate = normalRate;
        this.hourlyReducedRate = reducedRate;
        this.reduced = reducedPeriods;
        this.normal = normalPeriods;
    }

    /**
     * Checks if two collections of periods are valid together
     * @param periods1
     * @param periods2
     * @return true if the two collections of periods are valid together
     */
    private boolean isValidPeriods(ArrayList<Period> periods1, ArrayList<Period> periods2) {
        Boolean isValid = true;
        int i = 0;
        while (i < periods1.size() && isValid) {
            isValid = isValidPeriod(periods1.get(i), periods2);
            i++;
        }
        return isValid;
    }

    /**
     * checks if a collection of periods is valid
     * @param list the collection of periods to check
     * @return true if the periods do not overlap
     */
    private Boolean isValidPeriods(ArrayList<Period> list) {
        Boolean isValid = true;
        if (list.size() >= 2) {
            Period secondPeriod;
            int i = 0;
            int lastIndex = list.size()-1;
            while (i < lastIndex && isValid) {
                isValid = isValidPeriod(list.get(i), ((List<Period>)list).subList(i + 1, lastIndex+1));
                i++;
            }
        }
        return isValid;
    }

    /**
     * checks if a period is a valid addition to a collection of periods
     * @param period the Period addition
     * @param list the collection of periods to check
     * @return true if the period does not overlap in the collecton of periods
     */
    private Boolean isValidPeriod(Period period, List<Period> list) {
        Boolean isValid = true;
        int i = 0;
        while (i < list.size() && isValid) {
            isValid = !period.overlaps(list.get(i));
            i++;
        }
        return isValid;
    }
    public BigDecimal calculate(Period periodStay) {
        int normalRateHours = periodStay.occurences(normal);
        int reducedRateHours = periodStay.occurences(reduced);
 
        BigDecimal normalCost = this.hourlyNormalRate.multiply(BigDecimal.valueOf(normalRateHours));
        BigDecimal reducedCost = this.hourlyReducedRate.multiply(BigDecimal.valueOf(reducedRateHours));
        BigDecimal baseCost = normalCost.add(reducedCost);
        

        if(this.kind == CarParkKind.VISITOR) //  if CarParkKind id visitor
        {
             if(baseCost.compareTo(BigDecimal.valueOf(10)) <= 0 ){
                return BigDecimal.ZERO;
             }
             else{
                BigDecimal payableCost = baseCost.subtract(BigDecimal.valueOf(10));
                return payableCost.multiply(BigDecimal.valueOf(0.5))
             }
        }
        else if (this.kind == CarParkKind.MANAGEMENT) //  if CarParkKind id mamagement
        { 
            return baseCost.compareTo(BigDecimal.valueOf(4)) < 0 ? BigDecimal.valueOf(4) : baseCost;
        }
        else if ( this.kind == CarParkKind.STAFF){
            if(baseCost.compareTo(BigDecimal.valueOf))
        }
          if (this.kind==CarParkKind.STAFF) //  if CarParkKind id staff
          {
            if (baseCost.compareTo(BigDecimal.valueOf(16)) > 0) {
                return BigDecimal.valueOf(16);
            } else {
                return baseCost;
            }
         }
         else if(this.kind == CarParkKind.STUDENT) //  if CarParkKind id student
         {
            if (baseCost.compareTo(BigDecimal.valueOf(5.50)) > 0) {
                BigDecimal excess = baseCost.subtract(BigDecimal.valueOf(5.50));
                BigDecimal reducedExcess = excess.multiply(BigDecimal.valueOf(0.75)); // 25% reduction
                return BigDecimal.valueOf(5.50).add(reducedExcess);
            } else {
                return baseCost;
            }
         }
    }
}
    
    
          
