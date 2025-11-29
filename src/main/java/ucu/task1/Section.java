package ucu.task1;

public abstract class Section {

    private final int denomination;
    private int amount;
    private Section nextSection;

    public Section(int denomination) {
        this.denomination = denomination;
        this.amount = 0;
    }

    public int getDenomination() {
        return denomination;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = Math.max(amount, 0);
    }

    public void setNextSection(Section nextSection) {
        this.nextSection = nextSection;
    }

    public boolean hasNextSection() {
        return nextSection != null;
    }

    public Section getNextSection() {
        return nextSection;
    }

    public boolean canProcess(int number) {
        int maxQuantity = number / denomination;
        int usingQuantity = Math.min(maxQuantity, amount);
        int left = number - (usingQuantity * denomination);

        if (left == 0) {
            return true;
        }
        if (!hasNextSection()) {
            return false;
        }
        return nextSection.canProcess(left);
    }

    public void process(int number) {
        int maxQuantity = number / denomination;
        int usingQuantity = Math.min(maxQuantity, amount);
        int left = number - (usingQuantity * denomination);

        amount -= usingQuantity;

        if (usingQuantity > 0) {
            System.out.println("Denomination: " + denomination + ". Quantity: " + usingQuantity);
        }

        if (left != 0 && hasNextSection()) {
            nextSection.process(left);
        }
    }

    public int getTotalBalance() {
        int self = denomination * amount;
        int next = hasNextSection() ? nextSection.getTotalBalance() : 0;
        return self + next;
    }

}