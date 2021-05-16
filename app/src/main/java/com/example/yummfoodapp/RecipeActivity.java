package com.example.yummfoodapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.yummfoodapp.Adapter.ProductAdapter;
import com.example.yummfoodapp.Adapter.ProductCategoryAdapter;
import com.example.yummfoodapp.Adapter.RecipeAdapter;
import com.example.yummfoodapp.Model.Products;
import com.example.yummfoodapp.Model.Recipes;

import java.util.ArrayList;
import java.util.List;

public class RecipeActivity extends AppCompatActivity {

    RecyclerView productCatRecycler, prodItemRecycler;
    RecipeAdapter recipeAdapter;
    List<Recipes> RecipeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe);

        EditText editText= findViewById(R.id.searchFilter);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                recipeAdapter.filterList(filter(s.toString(),RecipeList));

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


        RecipeList.add(new Recipes (1, "Butter Chicken", "Level:Easy  Cook Time:35 Mins", R.drawable.butterchicken,"Method Details:\n" +
                "Ingredients:Chicken,1 large Onion,3 garlic cloves,1 green chilli\n"+
                "STEP 1\n" +
                " In a medium bowl, mix all the marinade ingredients with some seasoning. Chop the chicken into bite-sized pieces and toss with the marinade. Cover and chill in the fridge for 1 hr or overnight.\n" +
                "\n" +
                "STEP 2\n" +
                "In a large, heavy saucepan, heat the oil. Add the onion, garlic, green chilli, ginger and some seasoning. Fry on a medium heat for 10 mins or until soft.\n"+
                "STEP 3\n"+
                "Add the spices with the tomato purée, cook for a further 2 mins until fragrant, then add the stock and marinated chicken. Cook for 15 mins, then add any remaining marinade left in the bowl. Simmer for 5 mins," +
                " then sprinkle with the toasted almonds. Serve with rice, naan bread, chutney, coriander and lime wedges, if you like."

        ));

        RecipeList.add(new Recipes (1, " Thai Fried prawn & pineapple rice", "Level:Easy  Cook Time:15 Mins", R.drawable.pineapple,"Method Details:\n" +"STEP 1\n" +
                 "Heat the oil in a wok or non-stick frying pan and fry the spring onion whites for 2 mins until softened. Stir in the pepper for 1 min, followed by the pineapple for 1 min more, then stir in the green curry paste and soy sauce.\n" +
                 "\n" +
                 "STEP 2\n" +
                 "Add the rice, stir-frying until piping hot, then push the rice to one side of the pan and scramble the eggs on the other side. Stir the peas, bamboo shoots and prawns into the rice and eggs, then heat through for 2 mins until the prawns are hot and the peas tender. Finally, stir in the spring onion greens, lime juice and coriander, if using. Spoon into bowls and serve with extra lime wedges and soy sauce."));
        RecipeList.add(new Recipes (1, "Chicken Fajitas", "Level:Easy Cook Time:10 Mins ", R.drawable.chickenf,"Method Details:\n" +
               "Ingredients:Chicken,1 red Onion,1 red pepper, sliced,1 red chilli\n"+
               "STEP 1\n" +
               "Heat oven to 200C/180C fan/gas 6 and wrap 6 medium tortillas in foil.\n" +
               "STEP 2\n" +
               "Mix 1 heaped tbsp smoked paprika, 1 tbsp ground coriander, a pinch of ground cumin, 2 crushed garlic cloves, 4 tbsp olive oil, the juice of 1 lime and 4-5 drops Tabasco together in a bowl with a big pinch each of salt and pepper.\n" +

               "STEP 3\n" +
               "Stir 2 finely sliced chicken breasts, 1 finely sliced red onion, 1 sliced red pepper and 1 finely sliced red chilli, if using, into the marinade.\n" +

               "STEP 4\n" +
               "Heat a griddle pan until smoking hot and add the chicken and marinade to the pan.\n" +

               "STEP 5\n" +
               "Keep everything moving over a high heat for about 5 mins using tongs until you get a nice charred effect. If your griddle pan is small you may need to do this in two batches.\n" +

               "STEP 6\n" +
               "To check the chicken is cooked, find the thickest part and tear in half – if any part is still raw cook until done."

               ));
        RecipeList.add(new Recipes (1, "pork,cider & sage hotpot", "Level:Easy  Cook Time:15 Mins", R.drawable.porkhotpot,"Method Details:\n"+
               "Ingredients:Pork shoulder,butter,leeks,1 dry cider, chicken stock\n"+
               "STEP 1\n" +
               "Heat half of the oil in a deep ovenproof frying pan, or flameproof casserole dish, and fry the pork pieces over a medium high heat in batches until seared all over, then transfer to a plate. Add another 1 tbsp oil to the pan, if you need to, while you’re cooking the batches. Once all the pork is seared, transfer to a plate and set aside.\n" +

               "STEP 2\n" +
               "Add another 1 tbsp oil to the pan with a little butter and fry half the leeks with a pinch of salt for 10 mins until tender. Add the garlic, fry for a minute, then stir in the flour.\n" +

               "STEP 3\n" +
               "Pour in the cider, a little at a time, stirring to pick up any bits stuck to the bottom of the pan and to combine everything. Add the stock, bay leaves and seared pork, then simmer, half-covered with a lid for 1-1½ hrs until the meat is just tender (it will later cook to the point of falling apart in the oven). Can be prepared a day ahead.\n" +

               "STEP 4\n" +
               "Heat the oven to 200C/180C fan/gas 6. Simmer uncovered for a few minutes to reduce the sauce, if you need to – it shouldn’t be too liquid or the potatoes will sink into the sauce. Stir in the parsley, chopped sage, remaining leeks, and the cream, then season well.\n" +

               "STEP 5\n" +
               "Peel both types of potatoes and cut into slices 2mm thick, by hand or using a mandoline. Alternate layers of potato and sweet potato in circles over the pie, or randomly, if you prefer. Dot the cubed butter over the top and bake for 1-1½ hrs until the potato is tender. Nestle in the whole sage leaves, brushed in a little oil, for the last 10 mins. Leave to rest for 10 mins before serving."));
        RecipeList.add(new Recipes (1, "Pan-fried Steak", "Level:Easy  Cook Time:10 Mins", R.drawable.panfriedsteak,"Method Details:\n"+
                "Ingredients:Rib eye,butter,garlic clove,\n"+
                "STEP 1\n" +
                "Up to 8 hrs before cooking, pat the steaks dry with kitchen paper and season with salt and pepper. Heat the oil over a high flame in a heavy-based frying pan that will comfortably fit both steaks. When the oil is shimmering, turn the heat down to medium-high and add the butter. Once it’s sizzling, carefully lay the steaks in the pan, tucking the garlic and herbs in at the sides.\n" +
                "\n" +
                "STEP 2\n" +
                "Stand over the steaks with a pair of tongs, searing and turning them every 30 seconds to 1 min so they get a nice brown crust. As a rough guide, each steak will take 4 mins in total for rare, 5-6 mins in total for medium and 8-10 mins for well done. If you have a digital cooking thermometer, the temperatures you're looking for in middle of the steak are 50C for rare, 60C for medium and 70C for well done. Leave the steaks to rest for at least 5 mins. While the steaks are resting, you can make a classic red wine sauce to go with them."));

        RecipeList.add(new Recipes (1, "pumpkin Cheesecake", "Level:More effort time:1.45hrs", R.drawable.pumpkincheesecake,"Method Details:\n"+
                "Ingredients:butter, ginger biscuits, egg white, chesse, pumpkin puree, flavours\n"+
               "STEP 1\n" +
                "Heat the oven to 220C/200C fan/gas 7. Butter a deep 22cm loose-bottomed cake tin and line with baking parchment. Wrap the base and side of the tin with three layers of cling film, followed by three layers of foil (this helps keep it waterproof during baking). Fold a clean tea towel and put it in the base of a large roasting tin.\n" +

                "STEP 2\n" +
                "Blitz the biscuits to crumbs in a food processor. Add the melted butter and pulse to coat the crumbs. Tip the mixture into the prepared cake tin, spreading up to the side and pressing down with the back of a spoon. Bake for 10 mins. Remove from the oven, brush with the egg white and bake for another 3 mins (this will help stop the base from becoming soggy).\n" +

                "STEP 3\n" +
                "To make the filling, put the soft cheese in a bowl or the bowl of a stand mixer, and beat with an electric whisk or the mixer until loosened. Add the pumpkin purée, sugar and flour and beat again until combined. With the motor running, gradually add the eggs and egg yolk until the mixture is smooth and creamy. Pour the filling over the baked biscuit base, then sit the cake tin on the tea towel in the roasting tin. Pour a kettleful of just-boiled water into the roasting tin so the water comes halfway up the side of the cake tin.\n" +

                "STEP 4\n" +
                "Bake for 10 mins, then reduce the oven temperature to 110C/90C fan/gas ¼. Bake for a further 1 hr 30 mins until the cheesecake is set with just a slight wobble in the middle when you gently shake the tin. Turn off the oven, then open the oven door slightly and leave the cheesecake to cool inside for 2 hrs until completely cool. Remove from the oven and chill overnight.\n" +

                "STEP 5\n" +
                "Carefully remove the cold cheesecake from the tin and transfer to a cake stand or serving plate. Whip the cream to soft peaks using an electric whisk, and spoon in big dollops over the cheesecake. Dust with a little cinnamon or pumpkin spice, sprinkle over the chopped pecans and drizzle with the caramel sauce before serving."));


        RecipeList.add(new Recipes (1, "Grilled lobster", "Level:More effort time:1.45hrs", R.drawable.lobster,"Method Details:\n"+
                "Ingredients:lobster tails, lemon wedges, butter, garlic clove, parsley,mustard, chill powder\n"+
                "STEP 1\n" +
                "Make the butter by mixing together all the ingredients, then season and set aside. Can be made two days ahead. Remove from the fridge to soften before using.\n" +
                "STEP 2\n" +
                "Use kitchen scissors to cut along the tops of the lobster shells, then flip the tails over and crack the ribs of the shell. Use your fingers to open the shell and loosen the meat keeping it attached at the base and pull it half out. Use a knife to cut along the top of the tail without cutting all the way through and remove the vein if you see one. Sit the tail in a shallow roasting tray and add some butter to each one. Tails can be prepared a few hours ahead and chilled.  \n" +
                "STEP 3\n" +
                "Heat the grill to high, then grill the lobster tails for 10 mins until cooked through. Put them on plates and drizzle with the butter from the pan, or pour the butter into a ramekin and put it in the middle of the table for dipping the lobster meat in. Serve with lemon wedges and scatter with extra parsley, if you like. "));
        RecipeList.add(new Recipes (1, "Smokey chorizo&manchego quiche", "Level:More effort time:1.1 hours", R.drawable.smokychorizo,"Method Details:\n"+
                "Ingredients:chorizo, eggs,creame fraiche,double cream, cheese, flavours\n"+
                "STEP 1\n" +
                "Heat the oil in a medium frying pan over a high heat, and fry the chorizo for 4-5 mins until crisp. Transfer to a plate lined with kitchen paper using a slotted spoon, and set aside to cool. Set the pan with the fat aside, and leave to cool. For the pastry, put the flour, 1/2 tsp salt, the paprika, cayenne pepper, cooled chorizo fat and the butter in the bowl of a large food processor and blitz until the mixture looks like coarse breadcrumbs. Gradually pulse in 4 tbsp cold water until it comes together into a dough. Turn out onto a lightly floured surface, and knead gently until smooth. Wrap the dough and chill for at least 10 mins. Will keep chilled for up to three days.\n" +

                "STEP 2\n" +
                "Heat the oven to 200C/180C fan/gas 6. Roll the chilled pastry out on a floured work surface to the thickness of a 50p coin then lift it into a 23cm loose-bottomed tart tin, leaving an overhang. Prick the base all over with a fork, line with baking parchment, then fill with baking beans. Transfer to a baking sheet and bake for 15 mins. Remove the parchment and beans, then bake for 15-20 mins more until golden and crisp. Trim away the pastry that is overhanging using a serrated knife. \n" +

                "STEP 3\n" +
                "Meanwhile, whisk the eggs, crème fraîche and cream together, season, then stir in the cheese and cooled chorizo. Pour into the pastry case and bake for 25-30 mins until the filling is just set. Leave to cool in the tin, then remove from the tin and slice. Will keep in the fridge for up to three days"));
        RecipeList.add(new Recipes (1, "Suck stir-fry", "Level:easy time:12mins", R.drawable.duck,"Method Details:\n"+
                "Ingredients:duck breasts, onion, carrot, celery, vinegar , hoisin sauce\n"+
                "STEP 1\n" +
                "Heat a wok or a large frying pan over a high heat. Drizzle in 1/2 tbsp of the oil and stir-fry the duck for 5 mins until cooked through and golden. Set aside on a plate. Heat the remaining 1/2 tbsp oil and add the pak choi, onion, carrot and celery. Fry for another 5 mins until golden but still crunchy.\n" +

                "STEP 2\n" +
                "Mix the vinegar, five spice, hoisin, 6 tbsp water, and cornflour together in a small bowl. Add to the wok with the veg along with the cooked duck. Stir well and simmer for 2-3 mins until the sauce thickens and coats the veg. Scatter with the spring onions and serve with noodles or rice and the lemon wedges for squeezing over."));
        RecipeList.add(new Recipes (1, "Fish pie", "Level:easy time:40mins", R.drawable.fish,"Method Details:\n"+
                "Ingredients:butter, milk,butter,egg ,flour,mixed fish,parsley\n"+
                "STEP 1\n" +
                "Pour the milk into a large pan and add the flour and butter. Set over a medium heat and whisk continuously until you have a smooth, thick white sauce. Remove from the heat, add the mustard, most of the cheese (save a handful for the top), peas and parsley.\n" +
                "STEP 2\n" +
                "Meanwhile, boil the pasta in a large pan of water following pack instructions until just cooked. Drain.\n" +
                "STEP 3\n" +
                "Heat the oven to 200C/180C fan/gas 6. Tip the pasta into the sauce and add half the fish, stir everything together then tip into a large baking dish. Top with the rest of the fish, pushing it into the pasta a little, then scatter with the remaining cheese. Bake for 30 mins until golden, then serve with salad, if you like. Can be chilled and eaten within three days or frozen for up to a month. Defrost in the fridge, then reheat in a microwave or oven until piping hot."));



        setProdItemRecycler(RecipeList);
    }

    public ArrayList filter(String text, List<Recipes> RecipeList) {

        ArrayList<Recipes> filteredList = new ArrayList<>();

        for(Recipes item : RecipeList){
            if(item.getRecipeName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        return filteredList;
    }

    private void setProdItemRecycler(List<Recipes> recipesList) {
        prodItemRecycler = findViewById(R.id.productRecycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        prodItemRecycler.setLayoutManager(layoutManager);
        recipeAdapter = new RecipeAdapter(this, recipesList,false);
        prodItemRecycler.setAdapter(recipeAdapter);
    }


}