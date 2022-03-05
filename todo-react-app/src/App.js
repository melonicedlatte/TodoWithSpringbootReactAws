import React from "react";
import Todo from "./Todo";
import AddTodo from "./AddTodo";
import { Paper, List, Container } from "@material-ui/core";
import "./App.css";
import { call } from "./service/ApiServices";


class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      items: [
        // {
        //   id: "1",
        //   title: "Learn React",
        //   done: false
        // },
        // {
        //   id: "2",
        //   title: "Learn Redux",
        //   done: true
        // },
        // {
        //   id: "3",
        //   title: "Learn React Router",
        //   done: false
        // }
      ]
    };
  }

  // add = (item) => {
  //   const thisItems = this.state.items;
  //   item.id = "ID-" + thisItems.length;
  //   item.done = false;
  //   thisItems.push(item);
  //   this.setState({ items: thisItems });
  //   console.log("items : ", this.state.items);
  // };

  // delete = (item) => {
  //   const thisItems = this.state.items;
  //   console.log("Before Update Itesm : ", this.state.items);
  //   const newItems = thisItems.filter(e => e.id !== item.id); // 아이디 다른 얘들만 가져온다 == 아이디 같은 얘는 지운다 
  //   this.setState({ items: newItems }, () => {
  //     console.log("After Update Itesm : ", this.state.items);
  //   });
  // };

  // componentDidMount() {
  //   const requestOptions = {
  //     method: "GET",
  //     headers: { "Content-Type": "application/json" },
  //   };

  //   fetch("http://localhost:8080/todo/", requestOptions)
  //     .then(response => response.json())
  //     .then(
  //       (response) => {
  //         this.setState({ 
  //           items: response.data 
  //         });
  //       },
  //       (error) => {
  //         this.setState({ 
  //           error
  //         });
  //       }
  //     );
  // };

  componentDidMount() {
    call("/todo", "GET", null).then((response) =>
      this.setState({ items: response.data })
    );
  }

  add = (item) => {
    call("/todo", "POST", item).then((response) =>
      this.setState({ items: response.data })
    );
  };

  delete = (item) => {
    call("/todo", "DELETE", item).then((response) =>
      this.setState({ items: response.data })
    );
  };

  update = (item) => {
    call("/todo", "PUT", item).then((response) =>
      this.setState({ items: response.data })
    );
  };

  printAppItems = () => {
    console.log("부모 items : ", this.state.items);
  };

  render(){ 
    // idx 는 인덱스 값  
    var todoItems = this.state.items.length > 0 && (
      <Paper style={{ margin: 16 }}>
        <List>
          {this.state.items.map((item, idx) => (
            <Todo 
              item={item} 
              key={item.id} 
              delete={this.delete} 
              update={this.update}
              // printAppItems={this.printAppItems}
            />
          ))}
        </List>
      </Paper>
    );

    // 3. props로 넘겨주기
    return (
      <div className="App">
        <Container maxWidth="md">
          <AddTodo add={this.add} />
          <div className="TodoList">{todoItems}</div>
        </Container>
      </div>
    );
  }
};

export default App;
