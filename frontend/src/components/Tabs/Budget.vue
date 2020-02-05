<template>
  <div style="height: calc(100vh - 150px); background-color:#a7b8a3">
    <div style="padding:15px; width:50%; float: left;">
      <h4 style="color:#ffffff">Expenses:</h4>

      <div v-if="add && isOrganizer">
        <v-text-field v-model="name" label="Expense name" solo full-width dense>Name</v-text-field>
        <v-text-field v-model="cost" label="Cost" solo full-width dense>Cost</v-text-field>
        <v-btn block @click="addExpense">Add expense</v-btn>
        <br />
      </div>

      <div v-if="update && isOrganizer">
        <v-text-field v-model="expense.name" label="Expense name" solo full-width dense>Name</v-text-field>
        <v-text-field
          v-if="expense.children.length==0"
          v-model="expense.cost"
          label="Cost"
          solo
          full-width
          dense
        >Cost</v-text-field>
        <v-btn block @click="updateExpense">Update expense</v-btn>
        <br />
      </div>

      <v-treeview ref="tree" :items="expenses" :item-text="name" hoverable dark>
        <template v-slot:prepend="{item}">
          <h4 style="color:#ffffff">{{item.name}}</h4>
        </template>
        <template v-slot:append="{item}">
          <h7 style="color:#ffffff">{{item.cost}}</h7>
          <div v-if="isOrganizer">
            <i @click="updateBTN(item)" class="fas fa-edit" style="padding: 5px; color:#ffffff"></i>
            <i
              @click="deleteExpense(item)"
              class="fas fa-trash"
              style="padding: 5px; color:#ffffff"
            ></i>
            <i @click="addBTN(item)" class="fas fa-plus-circle" style="padding: 5px; color:#ffffff"></i>
          </div>
        </template>
      </v-treeview>
    </div>
    <div style="padding:15px; float: left; width:50%">
      <h4 style="color:#ffffff">Founds:</h4>
      <v-text-field v-model="founds" solo full-width dense></v-text-field>

      <h4 style="color:#ffffff">Founds left:</h4>
      <v-text-field v-model="leftFounds" solo full-width dense></v-text-field>

      <v-btn block v-if="isOrganizer" @click="updateBudget">Update budget</v-btn>
    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {
      founds: 0,
      budget: null,
      expenses: [],

      cost: null,
      name: "",

      add: false,
      update: false,

      expense: null
    };
  },
  mounted() {
    this.getBudget();
  },
  computed: {
    leftFounds() {
      if(this.expenses[0]!=null)
        return this.founds - this.expenses[0].cost;
      return 0;
    },
    isOrganizer: function() {
      return this.$store.getters.user.id == this.$store.getters.trip.organizer;
    }
  },
  methods: {
    addBTN(expenseItem) {
      this.add = !this.add;
      this.update = false;
      if (this.add) this.expense = expenseItem;
      else this.expense = null;
    },
    updateBTN(expenseItem) {
      this.update = !this.update;
      this.add = false;
      if (this.update) this.expense = expenseItem;
      else this.expense = null;
    },
    async getBudget() {
      axios
        .get(this.$url + "/" + this.$store.getters.trip.id + "/Budget", {
          headers: {
            Authorization: "Bearer:" + this.$store.getters.token
          }
        })
        .then(response => {
          this.founds = response.data.founds;
          this.expenses[0] = response.data.expense;
          this.budget = response.data;
          if (this.$refs["tree"]) this.$refs["tree"].$forceUpdate();
        })
        .catch(error => {
          this.showError(error);
        });
    },
    updateBudget() {
      (axios.defaults.headers.common["Authorization"] =
        "Bearer:" + this.$store.getters.token),
        axios
          .post(
            this.$url +
              "/" +
              this.$store.getters.trip.id +
              "/Budget/BudgetUpdate?" +
              "founds=" +
              this.founds
          )
          .then(response => {
            this.founds = response.data.founds;
            this.expenses[0] = response.data.expense;
            this.budget = response.data;
          })
          .catch(error => {
            this.showError(error);
          });
    },
    async addExpense() {
      (axios.defaults.headers.common["Authorization"] =
        "Bearer:" + this.$store.getters.token),
        axios
          .put(
            this.$url +
              "/" +
              this.$store.getters.trip.id +
              "/Budget/ExpenseAdd?" +
              "parentExpense=" +
              this.expense.id,
            {
              cost: this.cost,
              name: this.name
            }
          )
          .then()
          .catch(error => {
            this.showError(error);
          });
      this.add = false;
      this.name = "";
      this.cost = null;
      await new Promise(r => setTimeout(r, 200));
      this.getBudget();
    },
    async deleteExpense(expense) {
      axios
        .delete(
          this.$url +
            "/" +
            this.$store.getters.trip.id +
            "/Budget/ExpenseDelete",
          {
            headers: {
              Authorization: "Bearer:" + this.$store.getters.token
            },
            params: {
              expenseId: expense.id
            }
          }
        )
        .then(this.getBudget())
        .catch(error => {
          this.showError(error);
        });
      await new Promise(r => setTimeout(r, 200));
      this.getBudget();
    },
    async updateExpense() {
      (axios.defaults.headers.common["Authorization"] =
        "Bearer:" + this.$store.getters.token),
        axios
          .post(
            this.$url +
              "/" +
              this.$store.getters.trip.id +
              "/Budget/ExpenseUpdate?" +
              "expenseId=" +
              this.expense.id,
            {
              cost: this.expense.cost,
              name: this.expense.name
            }
          )
          .then(this.getBudget())
          .catch(error => {
            this.showError(error);
          });
      this.update = false;
      await new Promise(r => setTimeout(r, 200));
      this.getBudget();
    },
    showError: function(error) {
      if (error.response) {
        this.message = error.response.data.message;
      } else if (error.request) {
        this.message = error.request;
      } else {
        this.message = error;
      }
      this.$toasted.show(this.message, {
        theme: "outline",
        position: "top-right",
        duration: 1000
      });
    }
  }
};
</script>