<template>
  <div style="height: calc(100vh - 150px); background-color:#a7b8a3; padding: 20px">
    <list v-for="blog in blogs" v-bind:key="blog">
      <div id="ListElement" @click="setBlog(blog)" style="padding: 5px">
        <h5>{{blog.name}}</h5>
        <v-textarea
          v-model="blog.description"
          dense
          outlined
          dark
          rows="3"
          no-resize
          readonly
          style="font-size: 12px;"
        ></v-textarea>
      </div>
    </list>
    <v-pagination
      id="pagination"
      v-if="pages>1"
      @input="getBlogs()"
      v-model="page"
      color="#000000"
      :circle="true"
      :length="this.pages"
    ></v-pagination>
  </div>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {
      blogs: null,

      page: 1,
      pages: 0
    };
  },
  mounted() {
    this.getBlogs();
  },
  methods: {
    setBlog(blog) {
      this.$store.commit("setBlog", blog);
      this.$router.push({ path: "blog"});
    },
    getBlogs() {
      axios
        .get(this.$url + "/Blog/GetBlogs", {
          headers: {
            Authorization: "Bearer:" + this.$store.getters.token
          },
          params: {
            page: this.page - 1,
            size: 8
          }
        })
        .then(response => {
          this.pages = response.data.totalPages;
          this.blogs = response.data.content;
        })
        .catch(error => {
          this.showError(error);
        });
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

<style scoped>
#pagination {
}

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