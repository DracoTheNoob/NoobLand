package fr.dtn.noobland.feature.rank;

public class Rank {
    private final String id;
    private final String display;
    private final String simpleDisplay;
    private final int importance;

    protected static final Rank[] list = {
            new Rank("administrator", "§cAdministrateur", 10),
            new Rank("developer", "§aDéveloppeur", 8),
            new Rank("player", "§eJoueur§", 1)
    };

    private Rank(String id, String simpleDisplay, int importance){
        this.id = id;
        this.simpleDisplay = simpleDisplay;
        this.display = "§7[" + simpleDisplay + "§7]";
        this.importance = importance;
    }

    public Rank(String id){
        this.id = id;

        for(Rank rank : list) {
            if (rank.getId().equals(id)) {
                this.simpleDisplay = rank.getSimpleDisplay();
                this.display = rank.getDisplay();
                this.importance = rank.getImportance();
                return;
            }
        }

        this.simpleDisplay = null;
        this.display = null;
        this.importance = 0;
    }

    public String getId() { return id; }
    public String getSimpleDisplay() { return simpleDisplay; }
    public String getDisplay() { return display; }
    public int getImportance() { return importance; }
}