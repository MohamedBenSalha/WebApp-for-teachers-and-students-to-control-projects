<template>
  <div>
    <vs-row vs-justify="center">
      <vs-col type="flex" vs-justify="center" vs-align="center" :vs-lg="12" vs-sm="12" vs-xs="12"
              code-toggler>
        <vs-card class="cardx">
          <div slot="header">
            <h2 class="float-left" style="color: cornflowerblue">Submitted Registrations</h2>
          </div>
          <div class="table-responsive">
            <table class="table v-middle border">
              <thead>
              <tr class="">
                <th class="border-top-0" style="color: cornflowerblue">ID</th>
                <th class="border-top-0" style="color: cornflowerblue">Students</th>
                <th class="border-top-0" style="color: cornflowerblue">Projects</th>
                <th class="border-top-0" style="color: cornflowerblue">Creation Date</th>
                <th class="border-top-0" style="color: cornflowerblue">Actions</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="registration in registrations" :key="registration.id">
                <td>{{ registration.id }}</td>
                <td>
                  <ul v-for="student in registration.students" :key="student.id">
                    <li>{{ student.name }}</li>
                  </ul>
                </td>
                <td>{{ registration.preferredProjectIds }}</td>
                <td>{{ new Date(registration.created) }}</td>
                <td>
                  <div>
                    <vs-button @click="deleteForm = true, currentRegistrationID = registration.id  " icon="delete" class="m-1" color="danger"
                               type="filled">
                      Delete
                    </vs-button>
                    <vs-button @click='$router.push({name:"Registrations Edit", params: {registration: registration}});'
                               icon="edit" class="m-1" color="warning" type="filled">
                      Edit
                    </vs-button>
                  </div>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </vs-card>
      </vs-col>
      <vs-prompt
          title="Deletion"
          color="red"
          @close='notify("Closed","Deletion was cancelled.","warning")'
          @cancel='notify("Closed","Deletion was cancelled.","warning")'
          @accept="deleteSubmission(currentRegistrationID)"
          :is-valid="true"
          :active.sync="deleteForm"
      >
        <div class="con-exemple-prompt">
          Are you sure you want to delete the registration with the ID  <br>
          <h6>{{ currentRegistrationID }}</h6>
        </div>
      </vs-prompt>
    </vs-row>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: "employeeList",
  data: () => {
    return {

      registrations: [],
      currentRegistrationID:0,
      deleteForm:false,

    }
  },

  created: async function () {

    await this.fetchAllRegistrations()
  },

  computed: {},

  methods: {

    fetchAllRegistrations: async function () {
      await axios.get(`http://localhost:8080/api/v1/registrations`)
          .then(async (response) => {
            for (let i = 0; i < response.data.length; i++) {
              await axios.get(`http://localhost:8080/api/v1/registrations/` + (response.data[i]["id"]))
                  .then(response => {
                    this.registrations.push(response.data)
                  })
            }
          })
    },

    /** Shows prompt with title, message and selected color*/
    notify: function (title, message, color) {
      this.$vs.notify({
        title: title,
        text: message,
        color: color, type: "gradient",
      })
    },


    deleteSubmission: async function (id) {
      // send DELETE request to API to delete a specific cat by ID
      this.registrations = this.registrations.filter((project) => project.id !== id);
      await axios.delete(`http://localhost:8080/api/v1/registrations/` + id.toString())
      this.notify("Confirmation","Submission has been  successfully deleted","danger")
    },

  },

}

</script>

<style scoped>
.edit-employeee {
  color: red;
}
</style>
