package app.services;

import app.components.Component;
import app.promotions.Promotion;
import app.systems.Systems;

import java.util.ArrayList;

public class ProductsLists {

    static ArrayList<Component> componentsList = new ArrayList<>();
    static ArrayList<Systems> systemsList = new ArrayList<>();
    static ArrayList<Promotion> promotionsList = new ArrayList<Promotion>();


    public static void push(Component component) {
        componentsList.add(component);
    }
    public static void push(Systems systems) {
        systemsList.add(systems);
    }
    public static void push(Promotion promotion) {
        promotionsList.add(promotion);
    }

    public static int getComponentsAmount() {
        return componentsList.size();
    }
    public static Component getComponent (int i) {
        return componentsList.get(i);
    }

    public static int getSystemsAmount() {
        return systemsList.size();
    }
    public static Systems getSystems (int i) { return systemsList.get(i); }

    public static int getPromotionsAmount() {
        return promotionsList.size();
    }

    public static Promotion getPromotion (int systemId) {
        for (Promotion promotion : promotionsList) {
            if (promotion.systemId == systemId)
                return promotion;
        }
        return null;
    }

    public static int getSystemCategoryId(String category) {
        for (Systems systems : systemsList) {
            if (systems.categoryName == category)
                return systems.categoryId;
        }
        return 0;
    }

    public static int getComponentCategoryId(String category) {
        for (Component component : componentsList) {
            if (component.categoryName == category)
                return component.categoryId;
        }
        return 0;
    }

    public static Component getComponentById(int id) {
        for (Component component : componentsList) {
            if (component.categoryId == id)
                return component;
        }
        return null;
    }
}
