package id.adnan.noxcipes.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import id.adnan.noxcipes.R;
import id.adnan.noxcipes.config.DbConfig;
import id.adnan.noxcipes.fragments.RecipeDetailFragment;
import id.adnan.noxcipes.models.Recipe;

public class SearchRecipesAdapter extends RecyclerView.Adapter<SearchRecipesAdapter.SearchRecipesViewHolder> {
    private FragmentManager fragmentManager;
    private List<Recipe> recipes;
    private List<Recipe> recipesFull;

    public SearchRecipesAdapter(FragmentManager fragmentManager, List<Recipe> recipes, List<Recipe> recipesFull) {
        this.fragmentManager = fragmentManager;
        this.recipes = recipes;
        this.recipesFull = recipesFull;
    }

    public void searchRecipe(String query) {
        if (query.isEmpty()) {
            recipes.clear();
            notifyDataSetChanged();
            return;
        }

        ArrayList<Recipe> filteredList = new ArrayList<>();

        for (Recipe recipe : recipesFull) {
            if (recipe.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(recipe);
            }
        }

        recipes.clear();
        recipes.addAll(filteredList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_recipe, parent, false);
        return new SearchRecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRecipesViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.bind(recipe);

        holder.linearLayoutSearchRecipeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment(recipe.getId());
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, recipeDetailFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class SearchRecipesViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayoutSearchRecipeItem;
        private ImageView ivSearchRecipeImage;
        private TextView tvSearchRecipeTitle;
        private TextView tvSearchRecipeDifficulty;

        public SearchRecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayoutSearchRecipeItem = itemView.findViewById(R.id.linearLayout_search_recipe_item);
            ivSearchRecipeImage = itemView.findViewById(R.id.iv_search_recipe_image);
            tvSearchRecipeTitle = itemView.findViewById(R.id.tv_search_recipe_title);
            tvSearchRecipeDifficulty = itemView.findViewById(R.id.tv_search_recipe_difficulty);
        }

        public void bind(Recipe recipe) {
            Picasso.get().load(recipe.getImage()).into(ivSearchRecipeImage);
            tvSearchRecipeTitle.setText(recipe.getTitle());
            tvSearchRecipeDifficulty.setText(recipe.getDifficulty());
        }
    }
}
