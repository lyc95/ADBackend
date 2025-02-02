package nus.iss.ADBackend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;
import nus.iss.ADBackend.Service.IngredientService;
import nus.iss.ADBackend.Service.RecipeService;
import nus.iss.ADBackend.Service.UserService;
import nus.iss.ADBackend.helper.RecipeForm;
import nus.iss.ADBackend.model.Ingredient;
import nus.iss.ADBackend.model.Recipe;
import nus.iss.ADBackend.model.User;
import nus.iss.ADBackend.model.WeightedIngredient;;

@RestController
@RequestMapping(value = "/recipe", produces = "application/json")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class recipeController {

	@Autowired
	private RecipeService recipeService;
	@Autowired
	private IngredientService ingredientService;
	@Autowired
	private UserService userService;

	@GetMapping("/all")
	public List<Recipe> getStudents() {
		return recipeService.getAllRecipes();
	}

	@GetMapping("/ingredients/{keyword}")
	public List<Ingredient> getIngredient(@PathVariable String keyword) {
		List<Ingredient> res = ingredientService.findSimilarIngredients(keyword);
		// display max 10 result
		if (res.size() > 10) {
			res = new ArrayList<>(res.subList(0, 10));
		}
		return res;
	}

	@GetMapping("/ingredients")
	public List<Ingredient> getIngredient() {
		List<Ingredient> res = ingredientService.getAllIngredient();
		// display max 10 result
		if (res.size() > 10) {
			res = new ArrayList<>(res.subList(0, 10));
		}
		return res;
	}

	@GetMapping("/ingredient/{id}")
	public Ingredient getIngredient(@PathVariable int id) {
		return ingredientService.findIngredientById(id);
	}

	@PostMapping("/new")
	public ResponseEntity createRecipe(@RequestBody RecipeForm recipeForm) {
		int userId = recipeForm.getUserId();
		int portion = recipeForm.getPortion();
		List<String> procedures = recipeForm.getProcedures();
		List<WeightedIngredient> weightedIngredients = recipeForm.getWeightedIngredients();
		String name = recipeForm.getName();
		String imageDataUrl = recipeForm.getImage();
		if (recipeService.createRecipe(userId, weightedIngredients, procedures, imageDataUrl, portion, name)) {
			return new ResponseEntity<>(null, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping(value = "/{id}")
	public Recipe getrecipe(@PathVariable int id) {
		return recipeService.findRecipeById(id);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity editRecipe(@PathVariable int id, @RequestBody RecipeForm recipeForm) {
		int userId = recipeForm.getUserId();
		int portion = recipeForm.getPortion();
		List<String> procedures = recipeForm.getProcedures();
		List<WeightedIngredient> weightedIngredients = recipeForm.getWeightedIngredients();
		String name = recipeForm.getName();

		Recipe editedRecipe = recipeService.findRecipeById(id);
		String imageDataUrl = editedRecipe.getImage();

		if (recipeService.editRecipe(userId, weightedIngredients, procedures, imageDataUrl, portion, name,
				editedRecipe)) {
			return new ResponseEntity<>(null, HttpStatus.CREATED);
		}
		return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
	}

	@PostMapping("/checkuser")
	public ResponseEntity<User> authenticateUser(@RequestBody User user) {
		User u = userService.findUserByUserNameAndPassword(user.getUsername(), user.getPassword());
		if (u != null) {
			return ResponseEntity.ok(u);
		} else {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/android")
	public ResponseEntity<User> androidCheckUser(@RequestBody JSONObject userInput) {
		String userHash = userInput.getAsString("userhash");
		String passHash = userInput.getAsString("passhash");
		int userId = Integer.parseInt(userInput.getAsString("userid"));
		User u = userService.checkHashedUser(userId, userHash, passHash);
		if (u != null) {
			System.out.println("testing hash ok");
			return ResponseEntity.ok(u);
		} else {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(value = { "/search/{keyword}/{calories}", "/search/{calories}" })
	public List<Recipe> getRecipesBySearch(@PathVariable(required = false) String keyword, @PathVariable int calories) {
		List<Recipe> res;
		if (keyword == null || keyword.isEmpty()) {
			res = recipeService.getAllRecipes();
		} else {
			res = recipeService.findAllRecipesBySearch(keyword.trim());
		}
		return res.stream().filter(recipe -> recipe.getCalPerServing() <= calories).collect(Collectors.toList());

	}
}
