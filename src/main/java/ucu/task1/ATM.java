package ucu.task1;

public class ATM {

    private final Section firstSection;
    private final int smallestDenomination = 10;

    public ATM() {
        this.firstSection = new Section500();
        
        Section secondSection = new Section200();
        firstSection.setNextSection(secondSection);
        
        Section thirdSection = new Section100();
        secondSection.setNextSection(thirdSection);
        
        Section fourthSection = new Section50();
        thirdSection.setNextSection(fourthSection);
        
        Section fifthSection = new Section10();
        fourthSection.setNextSection(fifthSection);
        
        fillATM();
    }

    public void fillATM() {
        Section current = firstSection;
        while (current != null) {
            current.setAmount(100);
            current = current.getNextSection();
        }
    }

    public void giveMoney(int amount) {
        if (amount <= 0) {
            System.out.println("Requested amount must be positive.");
            return;
        }
        
        if (amount % smallestDenomination != 0) {
            System.out.println("This ATM can only dispense multiples of " + smallestDenomination + ".");
            return;
        }
        
        int totalBalance = firstSection.getTotalBalance();
        if (totalBalance < amount) {
            System.out.println("ATM doesn't have enough money.");
            System.out.println("Available " + totalBalance + ", requested " + amount);
            return;
        }

        if (!firstSection.canProcess(amount)) {
            System.out.println("Cannot provide the exact amount with available notes.");
            return;
        }
        
        firstSection.process(amount);
    }

}
