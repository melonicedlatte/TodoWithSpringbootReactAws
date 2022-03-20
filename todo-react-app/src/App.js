import React from "react";
import Todo from "./Todo";
import AddTodo from "./AddTodo.js";
import "./App.css";
import {
  Paper,
  List,
  Container,
  Grid,
  Button,
  AppBar,
  Toolbar,
  Typography,
} from "@material-ui/core";
import { call, signout } from "./service/ApiService"; // signout 추가

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
      ],
      /* 1. 로딩중이라는 상태이다. 생성자에 상태 변수를 추가한다. */
      loading: true,
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
      this.setState({ items: response.data, loading: false  })
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

  // idx == map에 사용되는 인덱스
  render() {
    var todoItems = this.state.items.length > 0 && (
      <Paper style={{ margin: 16 }}>
        <List> 
          {this.state.items.map((item, idx) => (
            <Todo
              item={item}
              key={item.id}
              delete={this.delete}
              update={this.update}
            />
          ))}
        </List>
      </Paper>
    );

    // navigationBar 추가
    var navigationBar = (
      <AppBar position="static">
        <Toolbar>
          <Grid justify="space-between" container>
            <Grid item>
              <Typography variant="h6">오늘의 할일</Typography>
            </Grid>
            <Grid>
              <Button color="inherit" onClick={signout}>
                로그아웃
              </Button>
            </Grid>
          </Grid>
        </Toolbar>
      </AppBar>
    );

    /* 로딩중이 아닐 때 렌더링 할 부분 */
    var todoListPage = (
      <div>
        {navigationBar} {/* 네비게이션 바 렌더링 */}
        <Container maxWidth="md">
          <AddTodo add={this.add} />
          <div className="TodoList">{todoItems}</div>
        </Container>
      </div>
    );

    /* 로딩중일 때 렌더링 할 부분 */
    var loadingPage = <h1> 로딩중.. </h1>;

    var content = loadingPage;

    if (!this.state.loading) {
      /* 로딩중이 아니면 todoListPage를 선택*/
      content = todoListPage;
    }

    /* 선택한 content 렌더링 */
    return <div className="App">{content}</div>;
  }

};

export default App;
