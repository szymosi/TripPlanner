<template>
  <div style="height: calc(100vh - 150px); background-color:#a7b8a3">
    <v-container>
      <v-row>
        <v-col cols="12" md="5">
          <v-text-field v-model="name" label="Task descryption"></v-text-field>
        </v-col>
        <v-col cols="12" md="2">
          <v-text-field v-model="deadline" label="Deadline"></v-text-field>
        </v-col>
        <v-col cols="12" md="4">
          <v-text-field v-model="user" label="User"></v-text-field>
        </v-col>
        <v-col cols="12" md="1">
          <i @click="addTask" class="fas fa-plus-circle fa-3x" style="padding: 5px"></i>
        </v-col>
      </v-row>
      <list v-for="task in tasks" v-bind:key="task">
        <b-list-group-item>
          <v-row>
            <v-col cols="12" md="1">
              <v-checkbox
                v-if="isOrganizer||task.user.id==$store.getters.user.id"
                v-model="task.finish"
                @mouseup="statusChange(task)"
              ></v-checkbox>
              <v-checkbox v-else disabled v-model="task.finish"></v-checkbox>
            </v-col>
            <v-col cols="12" md="4">
              <v-text-field v-if="isOrganizer" v-model="task.name" label="Task descryption"></v-text-field>
              <v-text-field v-else disabled v-model="task.name" label="Task descryption"></v-text-field>
            </v-col>
            <v-col cols="12" md="2">
              <v-text-field v-if="isOrganizer" v-model="task.deadline" label="Deadline"></v-text-field>
              <v-text-field v-else disabled v-model="task.deadline" label="Deadline"></v-text-field>
            </v-col>
            <v-col cols="12" md="2">
              <v-text-field v-if="isOrganizer" v-model="task.finishedDate" label="Finish Date"></v-text-field>
              <v-text-field v-else disabled v-model="task.finishedDate" label="Finish Date"></v-text-field>
            </v-col>
            <v-col cols="12" md="2">
              <v-text-field v-if="isOrganizer" v-model="task.user.username" label="User"></v-text-field>
              <v-text-field v-else disabled v-model="task.user.username" label="User"></v-text-field>
            </v-col>
            <v-col v-if="isOrganizer" cols="12" md="1">
              <i @click="updateTask(task)" class="fas fas fa-edit" style="padding: 5px"></i>
              <i @click="deleteTask(task)" class="fas fas fa-trash" style="padding: 5px"></i>
            </v-col>
          </v-row>
        </b-list-group-item>
      </list>
    </v-container>
    <v-pagination
      id="pagination"
      @input="getTasks"
      v-if="pages>1"
      v-model="page"
      color="#000000"
      :circle="true"
      :length="this.pages"
      style="padding-top: 15px"
    ></v-pagination>
  </div>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {
      tasks: null,

      name: "",
      user: "",
      deadline: "",

      page: 1,
      pages: 0
    };
  },
  mounted() {
    this.getTasks();
  },
  computed: {
    isOrganizer: function() {
      return this.$store.getters.user.id == this.$store.getters.trip.organizer;
    }
  },
  methods: {
    getTasks: function() {
      axios
        .get(this.$url + "/" + this.$store.getters.trip.id + "/Task", {
          headers: {
            Authorization: "Bearer:" + this.$store.getters.token
          },
          params: {
            page: this.page - 1,
            size: 6
          }
        })
        .then(response => {
          this.pages = response.data.totalPages;
          this.tasks = response.data.content;
        })
        .catch(error => {
          this.showError(error);
        });
      this.$forceUpdate();
    },
    async addTask() {
      (axios.defaults.headers.common["Authorization"] =
        "Bearer:" + this.$store.getters.token),
        axios
          .put(
            this.$url + "/" + this.$store.getters.trip.id + "/Task/TaskAdd",
            {
              deadline: this.deadline,
              name: this.name,
              user: this.user,
              finish: false
            }
          )
          .then(this.getTasks())
          .catch(error => {
            this.showError(error);
          });
      await new Promise(r => setTimeout(r, 200));
      this.getTasks();
    },
    async deleteTask(task) {
      axios
        .delete(
          this.$url + "/" + this.$store.getters.trip.id + "/Task/TaskDelete",
          {
            headers: {
              Authorization: "Bearer:" + this.$store.getters.token
            },
            params: {
              taskId: task.id
            }
          }
        )
        .then(this.getTasks())
        .catch(error => {
          this.showError(error);
        });
      await new Promise(r => setTimeout(r, 200));
      this.getTasks();
    },
    async updateTask(task) {
      (axios.defaults.headers.common["Authorization"] =
        "Bearer:" + this.$store.getters.token),
        axios
          .post(
            this.$url +
              "/" +
              this.$store.getters.trip.id +
              "/Task/TaskUpdate?" +
              "taskId=" +
              task.id,
            {
              deadline: task.deadline,
              name: task.name,
              user: task.user.username,
              finish: task.finish
            }
          )
          .then(this.getTasks())
          .catch(error => {
            this.showError(error);
          });
      await new Promise(r => setTimeout(r, 200));
      this.getTasks();
    },
    async statusChange(task) {
      await new Promise(r => setTimeout(r, 200));
      (axios.defaults.headers.common["Authorization"] =
        "Bearer:" + this.$store.getters.token),
        axios
          .post(
            this.$url +
              "/" +
              this.$store.getters.trip.id +
              "/Task/TaskStatus?" +
              "taskId=" +
              task.id +
              "&status=" +
              task.finish
          )
          .then(this.getTasks())
          .catch(error => {
            this.showError(error);
          });
      await new Promise(r => setTimeout(r, 200));
      this.getTasks();
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