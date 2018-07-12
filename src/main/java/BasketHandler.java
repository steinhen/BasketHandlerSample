import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class BasketHandler {

    private static final int[] PRICES = {10, 10, 2, 1, 1};
    private static final int[] VALUES = {6, 5, 4, 3, 3};
    private static final int MONEY = 11;
    private static final int EXPECTED_VALUE = 10;
    private static final boolean DEBUG = false;

    public static void main(String[] args) {

        int bestAffordableValue = getBestAffordableValue(PRICES, VALUES, MONEY);

        if (bestAffordableValue == EXPECTED_VALUE) {
            System.out.println("The best affordable value you can get is " + bestAffordableValue);
        } else {
            System.out.println("Value expected was " + EXPECTED_VALUE + ". Got " + bestAffordableValue + " instead.");
        }

    }

    private static int getBestAffordableValue(final int[] prices, final int[] values, final int money) {
        return getBestAffordableValue(
                getAllItemCombination(buildItemList(prices, values)),
                money
        );
    }

    private static List<Item> buildItemList(int[] prices, int[] values) {
        if (prices.length != values.length) {
            throw new RuntimeException("List of prices and values don't have the same size");
        }
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

        List<Item> previousCombinations = getAllItemCombination(items.subList(0, lastIndex));

        List<Item> result = new ArrayList<>(previousCombinations);
        previousCombinations.forEach(combination -> result.add(
                new Item(
                        combination.price + lastItem.price,
                        combination.value + lastItem.value
                )
        ));

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
                .orElse(new Item())
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
