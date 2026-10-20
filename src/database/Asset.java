package database;

import java.util.List;

public class Asset {

    public int assets_id;
    public String name;
    public int votes;
    
    //Enemy
    public int level;
    public int health;
    public int mana=5;
    public int damage;
    public int defense;
    public int speed;
    public int exp=5;
    public int money=5;
    
    //Skill
    public int value;
    public int cost;
    public String description;
    public int type;
    
    public String date;
    public int author;
    public int category;

    public List<AssetFile> files;

}