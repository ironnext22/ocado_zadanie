

# Features

## Configuration Loading

The _BasketSplitter_ constructor enables loading configuration from JSON file. Configuration contains mapping of products to available delivery methods.
```java
    public BasketSplitter(String absolutPathToConfigFile) {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            config = jsonb.fromJson(new FileReader(absolutPathToConfigFile), new HashMap<String, List<String>>() {
            }.getClass().getGenericSuperclass());
        }
        catch (Exception e) {e.printStackTrace();}

    }
```

## Basket Splitting

The **'split'** method takes a list of items, and returns a map that assigns products to delivery methods according to loaded configuration file

## Delivery Optimization
After splitting products, the application attempts to optimize deliveries by consolidating products that can be delivered together. This leads to a reduction in the number of deliveries and potentially reduces cost.

# Using the Application
To utilize the functionality of **"BasketSplitter"** you need to:

- Prepare a JSON file containing the configuration of products and delivery methods
- Create an instance of "BasketSplitter", passing the absolut path to the Json file as an argument
- Call the **'split'** method, passing a list of basket products as an argument

### Author
Author: **Wiktor Kowalski**