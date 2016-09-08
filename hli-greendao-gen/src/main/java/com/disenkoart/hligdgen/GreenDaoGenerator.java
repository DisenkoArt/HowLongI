package com.disenkoart.hligdgen;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGenerator {

    private static final int VERSION = 1;
    private static final String PACKAGE_DESTINATION = "com.disenkoart.howlongi.database";
    private static final String DESTINATION_FOLDER = "./app/src/main/java";

    private static final String GRADIENTS_TABLE = "Gradients";
    public static final String START_COLOR = "startColor";
    public static final String END_COLOR = "endColor";
    public static final String TIMERS_TABLE = "Timers";
    public static final String HLI_STRING = "hliString";
    public static final String START_DATE_TIME = "startDateTime";
    public static final String IS_ARCHIVED = "isArchived";
    public static final String GRADIENT_ID = "gradientId";

    public static void main(String[] args){
        Schema schema = new Schema(VERSION, PACKAGE_DESTINATION);
        createTables(schema);
        try {
            new DaoGenerator().generateAll(schema, DESTINATION_FOLDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createTables(Schema schema){
        Entity gradientsEntity = createGradientsTable(schema);
        Entity timersEntity = createTimersTable(schema);
        Property gradientProperty = timersEntity.addIntProperty(GRADIENT_ID).notNull().getProperty();
        timersEntity.addToOne(gradientsEntity, gradientProperty);
    }

    private static Entity createGradientsTable(Schema schema){
        Entity gradientsEntity = schema.addEntity(GRADIENTS_TABLE);
        gradientsEntity.addIdProperty().primaryKey().autoincrement().notNull();
        gradientsEntity.addIntProperty(START_COLOR).notNull();
        gradientsEntity.addIntProperty(END_COLOR).notNull();
        return gradientsEntity;
    }

    private static Entity createTimersTable(Schema schema){
        Entity timersEntity = schema.addEntity(TIMERS_TABLE);
        timersEntity.addIdProperty().primaryKey().autoincrement().notNull();
        timersEntity.addStringProperty(HLI_STRING).notNull();
        timersEntity.addIntProperty(START_DATE_TIME).notNull();
        timersEntity.addIntProperty(IS_ARCHIVED).notNull();
        return timersEntity;
    }

}
