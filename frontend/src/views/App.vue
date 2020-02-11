<template>
  <div>
    <div id="list">
      <TripsList></TripsList>
    </div>
    <div id="content">
      <BlogsList></BlogsList>
    </div>
  </div>
</template>

<script>
import TripsList from "@/components/TripsList";
import BlogsList from "@/components/BlogsList";
import axios from "axios";
export default {
  components: {
    TripsList,
    BlogsList
  },
  mounted() {
    this.interval = setInterval(() => this.refreshToken(), 3 * 60 * 1000);
  },
  beforeDestroy() {
    clearInterval(this.interval);
  },
  methods: {
    refreshToke() {
      if (this.$store.getters.token != "") {
        return axios
          .get(this.$url + "/User/RefreshToken", {
            headers: {
              Authorization: "Bearer:" + this.$store.getters.token
            }
          })
          .then(response => {
            this.$store.commit("setToken", response.data);
          });
      }
    }
  }
};
</script>

<style scoped>
#list {
  float: left;
  background-color: #8ea189;
  width: 7cm;
  height: 100%;
  align-content: left;
  text-align: left;
}
#content {
  float: left;
  width: calc(100% - 7cm);
}
</style>