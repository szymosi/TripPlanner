<template>
  <div style="height: calc(100vh - 150px); background-color:#a7b8a3">
    <div style="padding:30px; width:40%; height:100%; float: left;">
      <v-text-field
        v-if="isOrganizer"
        clearable
        label="Point name"
        v-model="name"
        :rules="nameRules"
        counter="50"
      ></v-text-field>
      <gmap-autocomplete
        v-if="isOrganizer"
        placeholder="Enter location"
        @place_changed="setPlace"
        style="width:100%"
      ></gmap-autocomplete>
      <br />
      <v-btn v-if="isOrganizer" color="#548561" @click="addPointBtn" large block>Add point</v-btn>
      <br />
      <b-list-group>
        <list v-for="controlPoint in controlPoints" v-bind:key="controlPoint">
          <b-list-group-item
            id="ListElement"
            button
            @click="selectPoint(controlPoint)"
          >{{controlPoint.order}} {{controlPoint.name}}</b-list-group-item>
        </list>
      </b-list-group>
      <v-pagination
        id="pagination"
        @input="getControlPoints(page-1)"
        v-if="pages>1"
        v-model="page"
        color="#000000"
        :circle="true"
        :length="this.pages"
        style="padding-top: 15px"
      ></v-pagination>

      <div v-if="controlPoint&&isOrganizer" style="padding-top: 15px">
        <v-text-field label="Order" v-model="controlPoint.order" style="width: 50px; float: left"></v-text-field>

        <i
          class="fas fa-map-marked-alt fa-2x"
          @click="changeOrder(controlPoint.order)"
          style="float: left; color: #ffffff"
        ></i>
        <h4>{{controlPoint.name}}</h4>

        <v-btn
          v-if="isOrganizer"
          id="deletePointbtn"
          retain-focus-on-click
          color="#548561"
          block
        >Remove point from trip</v-btn>
        <b-popover target="deletePointbtn" triggers="focus" placement="bottom">
          <h6>Are you sure you want to remove point from trip</h6>
          <b-button @click="delPointBtn" style="background-color: #687864;">Yes</b-button>
        </b-popover>
      </div>
    </div>
    <gmap-map :center="center" :zoom="10" style="width:60%; height: 100%; float: left;">
      <gmap-marker :key="index" v-for="(m, index) in markers" :position="m.position"></gmap-marker>
    </gmap-map>
  </div>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {
      center: { lat: 52.22977, lng: 21.01178 },
      markers: [],
      place: null,

      controlPoint: null,

      controlPoints: [],

      page: 1,
      pages: 0,

      name: "",
      nameRules: [
        v => v.length <= 50 || "Name must be maximum 50 characters long"
      ],

      message: ""
    };
  },
  computed: {
    isOrganizer: function() {
      return this.$store.getters.user.id == this.$store.getters.trip.organizer;
    }
  },
  mounted() {
    this.geolocate();
    this.getAllControlPoints();
    this.getControlPoints(0);
  },
  methods: {
    async addPointBtn() {
      if (this.place) {
        this.addControlPoint(
          this.place.geometry.location.lat(),
          this.place.geometry.location.lng()
        ),
          this.addMarker(),
          await new Promise(r => setTimeout(r, 200));
        this.getControlPoints(this.page - 1);
      }
    },
    selectPoint: function(point) {
      this.controlPoint = point;
      const marker = {
        lat: point.latitude,
        lng: point.longitude
      };
      this.center = marker;
    },
    async delPointBtn() {
      this.deleteControlPoint();
      await new Promise(r => setTimeout(r, 200));
      this.getControlPoints(this.page - 1);
      this.controlPoint = null;
    },
    setPlace: function(place) {
      this.place = place;
    },
    addMarker: function() {
      if (this.place) {
        const marker = {
          lat: this.place.geometry.location.lat(),
          lng: this.place.geometry.location.lng()
        };
        this.markers.push({ position: marker });
        this.center = marker;
      }
    },
    addMarkerLatLng: function(lat, lng) {
      const marker = {
        lat,
        lng
      };
      this.markers.push({ position: marker });
      this.center = marker;
    },
    geolocate: function() {
      navigator.geolocation.getCurrentPosition(position => {
        this.center = {
          lat: position.coords.latitude,
          lng: position.coords.longitude
        };
      });
    },
    getControlPoints: function(pageNr) {
      axios
        .get(this.$url + "/" + this.$store.getters.trip.id + "/Route", {
          headers: {
            Authorization: "Bearer:" + this.$store.getters.token
          },
          params: {
            page: pageNr,
            size: 8
          }
        })
        .then(response => {
          this.pages = response.data.totalPages;
          this.controlPoints = response.data.content;
        })
        .catch(error => {
          this.showError(error);
        });
      this.$forceUpdate();
    },
    getAllControlPoints: function() {
      axios
        .get(this.$url + "/" + this.$store.getters.trip.id + "/Route/GetAll", {
          headers: {
            Authorization: "Bearer:" + this.$store.getters.token
          }
        })
        .then(response => {
          this.message = response.data;
          response.data.forEach(element => {
            this.markers.push({
              position: { lat: element.latitude, lng: element.longitude }
            });
          });
        })
        .catch(error => {
          this.showError(error);
        });
    },
    deleteControlPoint: function() {
      axios
        .delete(
          this.$url +
            "/" +
            this.$store.getters.trip.id +
            "/Route/ControlPointDelete",
          {
            headers: {
              Authorization: "Bearer:" + this.$store.getters.token
            },
            params: {
              controlPointId: this.controlPoint.id
              //tripId: this.$store.getters.trip.id,
            }
          }
        )
        .then(
          this.$toasted.show("Control point deleted", {
            theme: "outline",
            position: "top-right",
            duration: 1000
          })
        )
        .catch(error => {
          this.showError(error);
        });
    },
    addControlPoint: function(lat, lng) {
      if (this.name == "") {
        this.name = lat + " " + lng;
      }
      (axios.defaults.headers.common["Authorization"] =
        "Bearer:" + this.$store.getters.token),
        axios
          .put(
            this.$url +
              "/" +
              this.$store.getters.trip.id +
              "/Route/ControlPointAdd",
            {
              latitude: lat,
              longitude: lng,
              name: this.name
            }
          )
          .then(response => {
            this.message = response;
          })
          .catch(error => {
            this.showError(error);
          });
    },
    changeOrder: function(pos) {
      (axios.defaults.headers.common["Authorization"] =
        "Bearer:" + this.$store.getters.token),
        axios
          .put(
            this.$url +
              "/" +
              this.$store.getters.trip.id +
              "/Route/ChangeOrder" +
              "?" +
              "controlPointId=" +
              this.controlPoint.id +
              "&newPosition=" +
              pos +
              "&page=" +
              (this.page - 1) +
              "&size=" +
              8
          )
          .then(response => {
            this.pages = response.data.totalPages;
            this.controlPoints = response.data.content;
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
list {
  list-style: none;
}

#ListElement {
  background-color: #687864;
  font-size: 16px;
  color: #eef7e9;
  border-radius: 0px;
  border: 0px;
  text-align: justify;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}

#ListElement:hover {
  background-color: #3e7a2f;
}

#ListElement:active {
  background-color: #000000;
}
</style>