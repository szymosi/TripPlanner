<template>
  <div id="app">
    <div id="nav">
    </div>
    <router-view style="height: 100%"></router-view>
  </div>
</template>


<style>

html, body {
  min-height: 100vh;
  min-width: 800px;
}

#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  min-height: 100vh;
  background-color: #a7b8a3
}


#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
</style>
