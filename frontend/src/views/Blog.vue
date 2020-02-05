<template>
  <div>
    <NavBar v-if="this.$store.getters.user==null"></NavBar>
    <NavBarLog v-else></NavBarLog>
    <list v-for="entry in entries" v-bind:key="entry">
      <div id="ListElement" style="padding: 5px">
          <v-row>
              <h4 style="text-align: left">{{entry.name}}</h4>
              <h6 style="text-align: right">{{entry.date}}</h6>
          </v-row>
          <p>{{entry.text}}</p>
      </div>
    </list>
    <v-pagination
      id="pagination"
      v-if="pages>1"
      @input="getEntries()"
      v-model="page"
      color="#000000"
      :circle="true"
      :length="this.pages"
    ></v-pagination>
  </div>
</template>

<script>
import NavBar from "@/components/NavBar";
import NavBarLog from "@/components/NavBarLog";
import axios from "axios";

export default {
  components: {
    NavBar,
    NavBarLog
  },
  data: function() {
    return {
      entries: null,

      page: 1,
      pages: 0
    };
  },
  mounted() {
    this.getEntries();
  },
  methods: {
    getEntries() {
      axios
        .get(this.$url + "/Blog/GetEntries", {
          headers: {
            Authorization: "Bearer:" + this.$store.getters.token
          },
          params: {
            blogId: this.$store.getters.blog.id,
            page: this.page - 1,
            size: 8
          }
        })
        .then(response => {
          this.pages = response.data.totalPages;
          this.entries = response.data.content;
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