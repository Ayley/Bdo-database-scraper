package me.kleidukos.object;

public record ItemStats(String attack, String defense, String accuracy, String evasion, String damageReduction) {
    @Override
    public String toString() {
        return "Stats{" +
                "attack='" + attack + '\'' +
                ", defense='" + defense + '\'' +
                ", accuracy='" + accuracy + '\'' +
                ", evasion='" + evasion + '\'' +
                ", damageReduction='" + damageReduction + '\'' +
                '}';
    }
}
