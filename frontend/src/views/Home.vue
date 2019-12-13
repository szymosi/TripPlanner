<template>
  <div class="home">
    <NavBar></NavBar>
    <img alt="Vue logo" src="../assets/logo.png">
  </div>
</template>

<script>
// @ is an alias to /src
import NavBar from '@/components/NavBar'

export default {
  name: 'home',
  components: {
    NavBar
  }
}
</script>
