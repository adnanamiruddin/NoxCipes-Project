package id.adnan.noxcipes.adapters;

import android.content.Context;
import android.content.Intent;
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

import java.util.List;

import id.adnan.noxcipes.R;
import id.adnan.noxcipes.config.DbConfig;
import id.adnan.noxcipes.fragments.RecipeDetailFragment;
import id.adnan.noxcipes.models.Recipe;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {
    private final FragmentManager fragmentManager;
    public List<Recipe> recipeList;
    private final int userId;

    public RecipesAdapter(FragmentManager fragmentManager, List<Recipe> recipeList, int userId) {
        this.fragmentManager = fragmentManager;
        this.recipeList = recipeList;
        this.userId = userId;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.bind(recipe, userId);

        holder.linearLayoutRecipeItem.setOnClickListener(new View.OnClickListener() {
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
        return recipeList.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout linearLayoutRecipeItem;
        private final ImageView ivRecipeImage;
        private final ImageView ivFavoriteButton;
        private final TextView tvRecipeTitle;
        private final TextView tvRecipeDifficulty;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayoutRecipeItem = itemView.findViewById(R.id.linearlayout_item_recipe);
            ivRecipeImage = itemView.findViewById(R.id.iv_recipe_image);
            ivFavoriteButton = itemView.findViewById(R.id.iv_favorite_button);
            tvRecipeTitle = itemView.findViewById(R.id.tv_recipe_title);
            tvRecipeDifficulty = itemView.findViewById(R.id.tv_recipe_difficulty);
        }

        public void bind(Recipe recipe, int userId) {
            Picasso.get().load(recipe.getImage()).into(ivRecipeImage);
            tvRecipeTitle.setText(recipe.getTitle());
            tvRecipeDifficulty.setText(recipe.getDifficulty());

            DbConfig dbConfig = new DbConfig(itemView.getContext());
            boolean isFavorite = dbConfig.isFavorite(userId, recipe.getId());
            ivFavoriteButton.setVisibility(isFavorite ? View.VISIBLE : View.GONE);
        }
    }
}
