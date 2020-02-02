<template>
  <div class="home">
    <NavBar v-if="this.$store.getters.user==null"></NavBar>
    <NavBarLog v-else></NavBarLog>
    <App v-if="this.$store.getters.user!=null" style="height: calc(100vh - 56px);"></App>
  </div>
</template>

<script>
import NavBar from "@/components/NavBar";
import NavBarLog from "@/components/NavBarLog";
import App from "@/views/TripApp";
import axios from "axios";

export default {
  components: {
    NavBar,
    NavBarLog,
    App
  },
  async mounted() {
    axios
      .get(this.$url + "/User/CurrentUser", {
        headers: {
          Authorization: "Bearer:" + this.$store.getters.token
        }
      })
      .then(response => {
        this.$store.commit("setUserDetails", response.data);
      })
      .catch(error => {
        if (error.response) {
          this.$store.commit("setUserDetails", null);
          this.$router.push({ path: "/" });
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
      });

    if (this.$route.query.trip != "") {
      axios
        .get(this.$url + "/Trip", {
          headers: {
            Authorization: "Bearer:" + this.$store.getters.token
          },
          params: {
            tripId: this.$route.query.trip
          }
        })
        .then(response => {
          this.$store.commit("setTrip", response.data);
        })
        .catch(error => {
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
        });

        await new Promise(r => setTimeout(r, 200));
        this.$forceUpdate()
    }
  }
};
</script>
