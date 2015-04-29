package tk.teemocode.commons.util.lang;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.MultiValueMap;

public class MoneyUtil {
	private MoneyUtil() {
	}

	/**
	 * 判断2个金额是否相等。因为存储精度的问题，当2个金额差额小于0.00000001时，就认为这2个金额相等
	 * @param money1
	 * @param money2
	 * @return
	 */
	public static boolean equals(Double money1, Double money2) {
		return (money1 == null && money2 == null) || (money1 != null && money2 != null && Math.abs(money1 - money2) < 0.00000001);
	}
	
	/**
	 * 判断money1是否大于money2。因为存储精度的问题，当money1比money2大0.00000001时返回true
	 * @param money1
	 * @param money2
	 * @return
	 */
	public static boolean biggerThan(Double money1, Double money2) {
		return !equals(money1, money2) && money1 > money2;
	}

	/**
	 * 修正价格，精确到2位小数
	 * @param price
	 * @return
	 */
	public static Double fixPrice(Double price) {
		return fixPrice(price, 2);
	}

	/**
	 * 修正价格，精确到指定的精确度
	 * @param price
	 * @return
	 */
	public static Double fixPrice(Double price, int scale) {
		if(!isValidPrice(price)) {
			return 0d;
		}
		return fixMoney(price, scale);
	}

	/**
	 * 修正折扣(以10为单位)，精确到10位小数
	 * @param discount
	 * @return
	 */
	public static Double fixDiscount(Double discount) {
		if(!isValidDiscount(discount)) {
			return 10d;
		}
		return fixMoney(discount, 10);
	}

	/**
	 * 修正金额，精确到2位小数
	 * @param price
	 * @return
	 */
	public static Double fixMoney(Double money) {
		return fixMoney(money, 2);
	}

	/**
	 * 修正金额到指定的精确度
	 * @param money
	 * @param scale
	 * @return
	 */
	public static Double fixMoney(Double money, int scale) {
		return ArithUtils.round(money, scale);
	}

	/**
	 * 判断一个价格是否有效(不为空且大于0)
	 * @param price
	 * @return
	 */
	public static boolean isValidPrice(Number price) {
		return price != null && price.doubleValue() > 0;
	}

	/**
	 * 判断一个价格是否合法(不为空且大于等于0)
	 * @param price
	 * @return
	 */
	public static boolean isLegalPrice(Number price) {
		return price != null && price.doubleValue() >= 0;
	}

	/**
	 * 判断一个折扣是否有效(不为空且大于0小于等于10)
	 * @param discount
	 * @return
	 */
	public static boolean isValidDiscount(Number discount) {
		return discount != null && discount.doubleValue() > 0 && discount.doubleValue() <= 10;
	}

	/**
	 *  获取一组金额(单价*数量)加权平均的结果, 可以有多个相同的单价值
	 * @param amounts
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Double getWeightedAverage(MultiValueMap amounts) {
		Double totalAmount = 0d;
		Long quantity = 0L;
		if(MapUtils.isNotEmpty(amounts)) {
			for(Double money : (Set<Double>) amounts.keySet()) {
				Collection<Integer> quantities = amounts.getCollection(money);
				for(Integer q : quantities) {
					Double amount = ArithUtils.mul(money, q.doubleValue());
					totalAmount = ArithUtils.add(totalAmount, amount);
					quantity += q;
				}
			}
		}
		return ArithUtils.div(totalAmount, quantity.doubleValue());
	}

	/**
	 * 获取一组金额(单价*数量)加权平均的结果
	 * @param amounts
	 * @return
	 */
	public static Double getWeightedAverage(HashMap<Double, Long> amounts) {
		Double totalAmount = 0d;
		Long quantity = 0L;
		if(MapUtils.isNotEmpty(amounts)) {
			for(Double money : amounts.keySet()) {
				Double amount = ArithUtils.mul(money, amounts.get(money).doubleValue());
				totalAmount = ArithUtils.add(totalAmount, amount);
				quantity += amounts.get(money);
			}
		}
		return ArithUtils.div(totalAmount, quantity.doubleValue());
	}

	/**
	 * 获取2个金额(单价*数量)加权平均的结果
	 * @param money1
	 * @param quantity1
	 * @param money2
	 * @param quantity2
	 * @return
	 */
	public static Double getWeightedAverage(Double money1, Long quantity1, Double money2, Long quantity2) {
		if(equals(money1, money2)) {
			return money1;
		}
		HashMap<Double, Long> amounts = new HashMap<Double, Long>();
		amounts.put(money1, quantity1);
		amounts.put(money2, quantity2);
		return MoneyUtil.getWeightedAverage(amounts);
	}

	public static double[] avgMoneyByDiscount(double money, double[] discounts, int scale){
		double[] results = new double[discounts.length];
		double sum=0, sumdiscount=0;
		for(int i=1; i<discounts.length; i++){
			sumdiscount+=discounts[i];
			results[i] = ArithUtils.round(money*discounts[i], scale);
			sum+=results[i];
		}

		discounts[0]=1-sumdiscount;
		results[0] = money-sum;
		return results;
	}
}
