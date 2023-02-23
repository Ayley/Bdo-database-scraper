package me.kleidukos.object.item;

public record ItemProperties(int minAttack, int maxAttack, int defense, int accuracy, int evasion, int hiddenEvasion, int damageReduction, int hiddenDamageReduction) {

    @Override
    public String toString() {
        return "ItemProperties{" +
                "minAttack=" + minAttack +
                ", maxAttack=" + maxAttack +
                ", defense=" + defense +
                ", accuracy=" + accuracy +
                ", evasion=" + evasion +
                ", hiddenEvasion=" + hiddenEvasion +
                ", damageReduction=" + damageReduction +
                ", hiddenDamageReduction=" + hiddenDamageReduction +
                '}';
    }
}
