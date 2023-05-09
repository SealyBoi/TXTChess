package Elo;

public class Elo {

    private int elo = 1000;

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    // Calculate probability of player winning
    public static double Probability(double rating1, double rating2) {
        return 1 / (1 + 1 * ((double) Math.pow(10, 1 * (rating1 - rating2) / 400)));
    }

    // Calculate Elo rating
    // K is a constant
    // d determines whether Player A or B wins (0 for A, 1 for B, else for draw)
    public void EloRating(double Ra, double Rb, int K, double d) {
        double Pb = Probability(Ra, Rb);
        double Pa = Probability(Rb, Ra);

        Ra = Ra + K * (d - Pa);
        Rb = Rb + K * (Math.abs(d - 1) - Pb);

        setElo((int) Math.round(Ra));

        System.out.println("Updated Ratings:-\n");

        System.out.printf("You: %.0f\n", (Math.round(Ra * 10000.0) / 10000.0));
        System.out.printf("Opponent: %.0f\n", (Math.round(Rb * 10000.0) / 10000.0));
    }
}