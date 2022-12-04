function Comment() {
    let stompClient = null;
    let messages = [];

    const connect = () => {
        const sockJS = new SockJS("http://localhost:8080/ws");
        stompClient = Stomp.over(sockJS);
        stompClient.connect({}, () => {
            stompClient.subscribe('/queue', (comment) => {
                if (document.getElementById(`like-${JSON.parse(comment.body).id}`)) {
                    const like = document.getElementById(`like-${JSON.parse(comment.body).id}`)
                    like.innerHTML = `Like ${JSON.parse(comment.body).liked}`
                } else {
                    const tr = document.createElement('tr')
                    const td = document.createElement('td')
                    const btn = document.createElement('button')
                    btn.id = `like-${JSON.parse(comment.body).id}`
                    btn.type = 'button'
                    btn.classList.add('btn-like')
                    btn.innerHTML = `Like ${JSON.parse(comment.body).liked}`
                    tr.append(td)
                    tr.append(btn)
                    td.append(JSON.parse(comment.body).content)
                    document.querySelector("#messages").prepend(tr)
                    document.querySelector(`#like-${JSON.parse(comment.body).id}`)
                        .addEventListener("click", (event) => {
                            const message = {
                                id: event.target.id.substring(5),
                            };
                            stompClient.send("/app/likes", {}, JSON.stringify(message));
                        })
                }
            })
        });
    };

    const sendMessage = async () => {
        const msg = document.querySelector("#message").value;

        if (msg.trim() !== "") {
            const message = {
                content: msg,
                timestamp: new Date(),
            };
            stompClient.send("/app/comments", {}, JSON.stringify(message));
        }

        document.querySelector("#message").value = "";
    };

    const getComments = async () => {
        return await fetch(`/api/comments`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(res => {
            return res.json()
        })
    }

    const drawComments = async () => {
        const comments = await getComments()
        comments.map((comment) => {
            const tr = document.createElement('tr')
            const td = document.createElement('td')
            const btn = document.createElement('button')
            btn.id = `like-${comment.id}`
            btn.type = 'button'
            btn.classList.add('btn-like')
            btn.append(`Like ${comment.liked}`)
            tr.append(td)
            tr.append(btn)
            td.append(comment.content)
            document.querySelector("#messages").append(tr)
        })
    }

    const init = async () => {
        connect()
        await drawComments()
        document.querySelector("#send").addEventListener("click", sendMessage)
        const likeBtns = document.querySelectorAll(".btn-like")
        likeBtns.forEach((likeBtn) => {
            likeBtn.addEventListener("click", (event) => {
                const message = {
                    id: event.target.id.substring(5),
                };
                stompClient.send("/app/likes", {}, JSON.stringify(message));
            })
        })
    }

    return {
        init
    }
}

const comment = new Comment()
comment.init()