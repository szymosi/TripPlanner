<template>
  <div>
    <v-row>
      <v-col cols="12" md="10">
        <h4 style="text-align: left">{{entry.name}}</h4>
      </v-col>
      <v-col cols="12" md="2">
        <h6 style="text-align: right">{{entry.date}}</h6>
      </v-col>
    </v-row>
    <p>{{entry.text}}</p>

    <div v-if="$store.getters.user">
      <v-textarea v-model="commentText" counter="500" solo dense no-resize></v-textarea>
      <v-btn @click="addComment()" color="#548561" block>Add comment</v-btn>
    </div>
    <br />
    <list v-for="comment in comments" v-bind:key="comment">
      <div
        style="border-style: solid; border-width: 1px; border-radius: 3px; border-color: #548561; padding: 5px"
      >
        <p style="text-align: left">{{comment.text}}</p>
        <v-row style="padding: 0px">
          <v-col cols="12" md="1">
            <i @click="deleteComment(comment)" class="fas fas fa-trash" style="padding: 2px"></i>
          </v-col>
          <v-col cols="12" md="9">
            <h6 style="text-align: right">{{comment.user.username}}</h6>
          </v-col>
          <v-col cols="12" md="2">
            <h6 style="text-align: right">{{comment.date}}</h6>
          </v-col>
        </v-row>
      </div>
    </list>
  </div>
</template>

<script>
import axios from "axios";
export default {
  props: {
    entry: null
  },
  data() {
    return {
      comments: null,
      commentText: "",

      page: 1,
      pages: 0
    };
  },
  mounted() {
    this.getComments();
  },
  methods: {
    getComments() {
      axios
        .get(this.$url + "/Comment", {
          headers: {
            Authorization: "Bearer:" + this.$store.getters.token
          },
          params: {
            entryId: this.entry.id,
            page: this.page - 1,
            size: 8
          }
        })
        .then(response => {
          this.pages = response.data.totalPages;
          this.comments = response.data.content;
        })
        .catch(error => {
          this.showError(error);
        });
    },
    async addComment() {
      (axios.defaults.headers.common["Authorization"] =
        "Bearer:" + this.$store.getters.token),
        axios
          .put(
            this.$url +
              "/Comment/CreateComment?" +
              "entryId=" +
              this.entry.id +
              "&text=" +
              this.commentText
          )
          .catch(error => {
            this.showError(error);
          });
      await new Promise(r => setTimeout(r, 200));
      this.getComments();
      this.commentText = "";
    },
    async deleteComment(comment) {
      axios
        .delete(this.$url + "/Comment/DeleteComment", {
          headers: {
            Authorization: "Bearer:" + this.$store.getters.token
          },
          params: {
            commentId: comment.id
          }
        })
        .catch(error => {
          this.showError(error);
        });
      await new Promise(r => setTimeout(r, 200));
      this.getComments();
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