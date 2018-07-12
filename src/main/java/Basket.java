import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class Basket {

    private static final int[] PRICES = {10, 10, 2, 1, 1};
    private static final int[] VALUES = {6, 5, 4, 3, 3};
    private static final int MONEY = 11;
    private static final int EXPECTED_VALUE = 10;
    private static final boolean DEBUG = false;

    public static void main(String[] args) {

        int basket = getBasket(PRICES, VALUES, MONEY);

        if (basket == EXPECTED_VALUE) {
            System.out.println("The best affordable value you can get is " + basket);
        } else {
            System.out.println("Value expected was " + EXPECTED_VALUE + ". Got " + basket + " instead.");
        }

    }

    private static int getBasket(final int[] prices, final int[] values, final int money) {
        return getBestAffordableValue(
                getAllItemCombination(buildItemList(prices, values)),
                money
        );
    }

    private static List<Item> buildItemList(int[] prices, int[] values) {
        List<Item> items = new ArrayList<>();
        IntStream.range(0, prices.length).forEach(i -> items.add(new Item(prices[i], values[i])));
        return items;
    }

    private static List<Item> getAllItemCombination(List<Item> items) {
        if (items.isEmpty()) {
            return Collections.singletonList(new Item());
        }

        int lastIndex = items.size() - 1;
        Item lastItem = items.get(lastIndex);

        List<Item> combinations = getAllItemCombination(items.subList(0, lastIndex));

        List<Item> result = new ArrayList<>(combinations);

        Item sum = new Item(lastItem.price, lastItem.value);
        for (Item combination : combinations) {
            result.add(new Item(combination.price + sum.price, combination.value + sum.value));
        }
        if (DEBUG) {
            result.forEach(System.out::println);
            System.out.println();
        }

        return result;
    }

    private static int getBestAffordableValue(List<Item> summedUpItems, int money) {
        return summedUpItems.stream()
                .filter(item -> item.price <= money)
                .max(Comparator.comparingInt(item -> item.value))
                .get()
                .value;
    }

    private static class Item {
        int price;
        int value;

        Item() {
        }

        Item(int price, int value) {
            this.price = price;
            this.value = value;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Item=[");
            sb.append("price=[").append(price).append(']');
            sb.append(", value=[").append(value).append(']');
            sb.append(']');
            return sb.toString();
        }
    }
}
