<template>
  <div style="height: calc(100vh - 150px); background-color:#a7b8a3; padding: 20px">
    <v-row>
      <v-col cols="12" md="6">
        <v-text-field v-if="isOrganizer" v-model="name" label="Blog name"></v-text-field>
        <v-text-field v-else disabled v-model="name" label="Blog name"></v-text-field>
        <v-radio-group v-if="isOrganizer" v-model="visibility">
          <v-radio label="Participants" value="participants"></v-radio>
          <v-radio label="Users" value="users"></v-radio>
          <v-radio label="Guests" value="guests"></v-radio>
        </v-radio-group>
        <v-radio-group v-else disabled light v-model="visibility">
          <v-radio label="Participants" value="participants"></v-radio>
          <v-radio label="Users" value="users"></v-radio>
          <v-radio label="Guests" value="guests"></v-radio>
        </v-radio-group>
      </v-col>
      <v-col cols="12" md="6">
        <v-textarea v-if="isOrganizer" clearable label="Blog descryption" v-model="description"></v-textarea>
        <v-textarea v-else disabled clearable label="Blog descryption" v-model="description"></v-textarea>
      </v-col>
    </v-row>
    <v-btn @click="updateBlog" v-if="isOrganizer" color="#548561" x-large block>Update blog</v-btn>
    <br />
    <v-btn @click="addBTN" v-if="isOrganizer&&add==false" color="#548561" x-large block>Add entry</v-btn>

    <div v-if="isOrganizer && entry && add">
      <v-text-field v-model="entry.name" label="Entry name"></v-text-field>
      <v-textarea v-model="entry.text" label="Entry text"></v-textarea>
      <v-btn @click="addEntry" color="#548561" block>Add entry</v-btn>
      <v-btn @click="add=false" color="#548561" block>Cancel</v-btn>
    </div>

    <div v-if="isOrganizer && entry && update">
      <v-text-field v-model="entry.name" label="Entry name"></v-text-field>
      <v-textarea v-model="entry.text" label="Entry text"></v-textarea>
      <v-btn @click="updateEntry" color="#548561" block>Update entry</v-btn>
      <v-btn @click="update=false" color="#548561" block>Cancel</v-btn>
    </div>

    <div v-if="!add&&!update" style="padding-left: 18px; padding-right: 18px">
      <br />
      <list v-for="entry in entries" v-bind:key="entry">
        <v-row id="ListElement">
          <v-col cols="12" md="11">
            <h4>{{entry.name}}</h4>
          </v-col>
          <v-col cols="12" md="1" v-if="isOrganizer">
            <i @click="updateBTN(entry)" class="fas fas fa-edit" style="padding: 2px"></i>
            <i @click="deleteEntry(entry)" class="fas fas fa-trash" style="padding: 2px"></i>
          </v-col>
        </v-row>
      </list>
      <v-pagination
        id="pagination"
        @input="getEntries()"
        v-if="pages>1"
        v-model="page"
        color="#000000"
        :circle="true"
        :length="this.pages"
      ></v-pagination>
    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {
      id: "",
      name: "",
      visibility: "participants",
      description: "",

      entries: [],
      entry: null,

      add: false,
      update: false,

      page: 1,
      pages: 0
    };
  },
  async mounted() {
    this.getBlog();
    await new Promise(r => setTimeout(r, 200));
    this.getEntries();
  },
  computed: {
    isOrganizer() {
      return this.$store.getters.user.id == this.$store.getters.trip.organizer;
    }
  },
  methods: {
    addBTN() {
      this.add = true;
      this.update = false;
      this.entry = { name: "", text: "" };
    },
    updateBTN(entry) {
      this.update = true;
      this.add = false;
      this.entry = entry;
    },
    getBlog() {
      axios
        .get(this.$url + "/" + this.$store.getters.trip.id + "/Blog", {
          headers: {
            Authorization: "Bearer:" + this.$store.getters.token
          }
        })
        .then(response => {
          this.id = response.data.id;
          this.name = response.data.name;
          this.visibility = response.data.visibility;
          this.description = response.data.description;
        })
        .catch(error => {
          this.showError(error);
        });
      this.$forceUpdate();
    },
    updateBlog() {
      (axios.defaults.headers.common["Authorization"] =
        "Bearer:" + this.$store.getters.token),
        axios
          .post(
            this.$url + "/" + this.$store.getters.trip.id + "/Blog/Update",
            {
              name: this.name,
              visibility: this.visibility,
              description: this.description
            }
          )
          .then(
            this.$toasted.show("Blog updated succesfully", {
              theme: "outline",
              position: "top-right",
              duration: 1000
            })
          )
          .catch(error => {
            this.showError(error);
          });
    },
    getEntries() {
      axios
        .get(
          this.$url + "/Blog/GetEntries",
          {
            headers: {
              Authorization: "Bearer:" + this.$store.getters.token
            },
            params: {
              blogId: this.id,
              page: this.page - 1,
              size: 5
            }
          }
        )
        .then(response => {
          this.pages = response.data.totalPages;
          this.entries = response.data.content;
        })
        .catch(error => {
          this.showError(error);
        });
    },
    async addEntry() {
      (axios.defaults.headers.common["Authorization"] =
        "Bearer:" + this.$store.getters.token),
        axios
          .put(
            this.$url + "/" + this.$store.getters.trip.id + "/Blog/AddEntry",
            {
              name: this.entry.name,
              text: this.entry.text
            }
          )
          .then(response => {
            this.$toasted.show(response.message, {
              theme: "outline",
              position: "top-right",
              duration: 1000
            });
          })
          .catch(error => {
            this.showError(error);
          });
      await new Promise(r => setTimeout(r, 200));
      this.getEntries();
      this.add = false;
      this.entry = null;
    },

    async deleteEntry(entry) {
      axios
        .delete(
          this.$url +
            "/" +
            this.$store.getters.trip.id +
            "/Blog/DeleteEntry",
          {
            headers: {
              Authorization: "Bearer:" + this.$store.getters.token
            },
            params: {
              entryId: entry.id
            }
          }
        )
        .catch(error => {
          this.showError(error);
        });
      await new Promise(r => setTimeout(r, 200));
      this.getEntries();
    },

    async updateEntry() {
      (axios.defaults.headers.common["Authorization"] =
        "Bearer:" + this.$store.getters.token),
        axios
          .post(
            this.$url +
              "/" +
              this.$store.getters.trip.id +
              "/Blog/UpdateEntry" +
              "?entryId=" +
              this.entry.id,
            {
              name: this.entry.name,
              text: this.entry.text
            }
          )
          .then(
            this.$toasted.show("Entry updated succesfully", {
              theme: "outline",
              position: "top-right",
              duration: 1000
            })
          )
          .catch(error => {
            this.showError(error);
          });
      await new Promise(r => setTimeout(r, 200));
      this.getEntries();
      this.update = false;
      this.entry = null;
    },
    showError(error) {
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

<style scoped>

#ListElement {
  text-align: left;
  background-color: #687864;
  font-size: 16px;
  color: #eef7e9;
  border-radius: 5px;
  border: 0px;
}

#ListElement:hover {
  background-color: #3e7a2f;
}
</style>