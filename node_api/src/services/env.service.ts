const Env = {
  API: process.env.VITE_API ? process.env.VITE_API : "http://localhost:8080/",
};

Object.freeze(Env);

export default Env;
