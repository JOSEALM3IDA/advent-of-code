package pt.josealm3ida.aoc.monkey_market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.josealm3ida.aoc.utils.Utils;

public class Main {

    private static int NTH_SECRET = 2000;

    public static void main(String[] args) {
        List<SecretNumber> secretNumbers = Utils.readLines("input/monkey-market.txt").stream().map(s -> new SecretNumber(Long.parseLong(s))).toList();

        System.out.println(part1(secretNumbers));
        System.out.println(part2(secretNumbers));
    }

    private static long part1(List<SecretNumber> secretNumbers) {
        return secretNumbers.stream().map(n -> n.getNth(NTH_SECRET).getNumber()).mapToLong(Long::valueOf).sum();
    }

    private static long part2(List<SecretNumber> secretNumbers) {
        Map<List<Integer>, Integer> sequences = new HashMap<>();

        for (SecretNumber n : secretNumbers) {
            List<Integer> allPrices = n.getAll().stream().map(s -> s.getPrice()).toList();

            int lastPrice;

            Map<List<Integer>, Integer> nrSequences = new HashMap<>();
            for (int i = 0; i < allPrices.size() - 3; i++) {
                List<Integer> sequence = new ArrayList<>();

                lastPrice = i == 0 ? n.getPrice() : allPrices.get(i - 1);

                for (int j = 0; j < 4; j++) {
                    int currPrice = allPrices.get(i + j);
                    sequence.add(currPrice - lastPrice);
                    lastPrice = currPrice;
                }

                if (!nrSequences.containsKey(sequence)) {
                    nrSequences.put(sequence, lastPrice);
                }
            }

            nrSequences.forEach((s, p) -> {
                sequences.put(s, sequences.getOrDefault(s, 0) + p);
            });
        }

        return Collections.max(sequences.entrySet(), Map.Entry.comparingByValue()).getValue();
    }

    private static class SecretNumber {
        private long number;

        public SecretNumber(long number) {
            this.number = number;
        }

        public SecretNumber getNth(int nth) {
            SecretNumber ns = new SecretNumber(this.number);

            for (int i = 0; i < nth; i++) {
                ns.toNext();
            }

            return ns;
        }
        
        public List<SecretNumber> getAll() {
            List<SecretNumber> list = new ArrayList<>();

            SecretNumber first = new SecretNumber(this.number);
            first.toNext();

            list.add(first);

            for (int i = 1; i < NTH_SECRET; i++) {
                SecretNumber next = new SecretNumber(list.get(i - 1).getNumber());
                next.toNext();
                list.add(next);
            }

            return new ArrayList<>(list);
        }

        public int getPrice() {
            return (int) this.number % 10;
        }

        private void toNext() {
            this.mix(this.number * 64);
            this.prune();
            this.mix(this.number / 32);
            this.prune();
            this.mix(this.number * 2048);
            this.prune();
        }

        private void mix(long with) {
            this.number = this.number ^ with;
        }

        // this number is divisible by 2048 (=8192) as well as 64 and 32 (262144, 524288)
        private void prune() {
            this.number = this.number % 16777216;
        }

		public long getNumber() {
			return number;
		}
    
        @Override
        public String toString() {
            return String.valueOf(this.number);
        }

    }

}
