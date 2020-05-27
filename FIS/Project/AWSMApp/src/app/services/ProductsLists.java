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

    public static Component getComponent(int i) {
        return componentsList.get(i);
    }

    public static int getSystemsAmount() {
        return systemsList.size();
    }

    public static Systems getSystems(int i) {
        return systemsList.get(i);
    }

    public static int getPromotionsAmount() {
        return promotionsList.size();
    }

    public static Promotion getPromotion(int systemId) {
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

    public static int deleteComponent(int id) {
        for (Component component : componentsList) {
            if (component.id == id) {
                componentsList.remove(component);
                return 1;
            }
        }
        return 0;
    }

    public static int deleteSystem(int id) {
        for (Systems system : systemsList) {
            if (system.id == id) {
                systemsList.remove(system);
                return 1;
            }
        }
        return 0;
    }

    public static int updateSystem(Systems updatedSystem) {
        int i=0;
        for (Systems system : systemsList) {
            if (system.id == updatedSystem.id) {
                systemsList.set(i, updatedSystem);
                return 1;
            }
            i++;
        }
        return 0;
    }

    public static int updateComponent(Component updatedComponent) {
        int i=0;
        for (Component component : componentsList) {
            if (component.id == updatedComponent.id) {
                componentsList.set(i, updatedComponent);
                return 1;
            }
            i++;
        }
        return 0;
    }

    public static Component getComponentById(int id) {
        for (Component component : componentsList) {
            if (component.id == id) {
                return component;
            }
        }
        return null;
    }

    public static void incrementComplaints(int id) {
        int amount = getComponentsAmount();
        for (int i = 0; i<amount; i++) {
            if (componentsList.get(i).id == id) {
                componentsList.get(i).complaints++;
                break;
            };
        }
    }
}
