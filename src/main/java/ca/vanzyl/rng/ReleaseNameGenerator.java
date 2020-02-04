package ca.vanzyl.rng;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ReleaseNameGenerator {

    private final List<String> adjectives;
    private final List<String> nouns;

    public ReleaseNameGenerator() {
        nouns = load("nouns.txt");
        adjectives = load("adjectives.txt");
    }

    public String generateRandom() {
        return randomReleaseName(adjectives, nouns);
    }

    public String generateStartingWith(String letter) {
        return constrainedReleaseName(adjectives, nouns, letter);
    }

    public String generateAlliterative() {
        return alliterativeReleaseName(adjectives, nouns);
    }

    private String releaseName(String adjective, String noun) {
        return adjective + "-" + noun;
    }

    private String randomReleaseName(List<String> adjectives, List<String> animals) {
        Random random = new Random();
        return releaseName(oneOf(adjectives, random), oneOf(animals, random));
    }

    private String constrainedReleaseName(List<String> adjectives, List<String> animals, String firstLetter) {
        Random random = new Random();
        return releaseName(oneOfStartingWith(adjectives, firstLetter, random), oneOfStartingWith(animals, firstLetter, random));
    }

    private String alliterativeReleaseName(List<String> adjectives, List<String> animals) {
        Random random = new Random();
        String adjective = oneOf(adjectives, random);
        String firstLetter = adjective.substring(0, 1);
        return releaseName(adjective, oneOfStartingWith(animals, firstLetter, random));
    }

    private String oneOf(List<String> words, Random random) {
        return words.get(random.nextInt(words.size()));
    }

    private String oneOfStartingWith(List<String> words, String firstLetter, Random random) {
        return oneOf(words.stream()
                        .filter(word -> word.startsWith(firstLetter))
                        .collect(Collectors.toList()),
                random);
    }

    private List<String> load(String resource) {
        BufferedReader br = new BufferedReader(new InputStreamReader(ReleaseNameGenerator.class.getClassLoader().getResourceAsStream(resource)));
        return br.lines()
                .map(String::trim)
                .filter(line -> line.length() > 1)
                .distinct()
                .collect(Collectors.toList());
    }

    public static void main(String[] args) throws Exception {
        ReleaseNameGenerator rng = new ReleaseNameGenerator();
        System.out.println(rng.generateRandom());
        System.out.println(rng.generateAlliterative());
        System.out.println(rng.generateStartingWith("y"));
    }
}
