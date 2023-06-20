package fr.dtn.noobland.feature.rank;

public class Rank {
    private final String id;
    private final String display;
    private final int importance;

    private static final Rank[] ranks = {
            new Rank("administrator", "§7[§cAdministrateur§7]", 10),
            new Rank("developer", "§7[§aDéveloppeur§7]", 8),
            new Rank("player", "§7[§eJoueur§7]", 1)
    };

    private Rank(String id, String display, int importance){
        this.id = id;
        this.display = display;
        this.importance = importance;
    }

    public Rank(String id){
        this.id = id;

        for(Rank rank : ranks) {
            if (rank.getId().equals(id)) {
                this.display = rank.getDisplay();
                this.importance = rank.getImportance();
                return;
            }
        }

        this.display = null;
        this.importance = 0;
    }

    public String getId() { return id; }
    public String getDisplay() { return display; }
    public int getImportance() { return importance; }
}