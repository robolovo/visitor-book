function Comment() {
    let stompClient = null;

    const getMessage = (content, buttonId, buttonText) => {
        return `
           <td>
               ${content}
               <button type="button" class="btn-like" id="${buttonId}">${buttonText}</button>
           </td>
       `;
    }

    const connect = () => {
        const sockJS = new SockJS("http://localhost:8080/ws");
        stompClient = Stomp.over(sockJS);
        stompClient.connect({}, () => {
            stompClient.subscribe('/like', (comment) => {
                const body = JSON.parse(comment.body)
                const like = document.getElementById(`like-${body.id}`)
                like.innerHTML = `Like ${body.liked}`
            }),
            stompClient.subscribe('/comment', (comment) => {
                const body = JSON.parse(comment.body)
                const content = body.content
                const buttonId = `like-${body.id}`
                const buttonText = `Like ${body.liked}`

                const messages = document.querySelector("#messages")
                const message = document.createElement("tr");
                message.innerHTML = getMessage(content, buttonId, buttonText)
                messages.prepend(message)

                document.querySelector(`#${buttonId}`)
                    .addEventListener("click", (event) => {
                        const message = {
                            id: event.target.id.substring(5),
                            type: 'like'
                        };
                        stompClient.send("/app/likes", {}, JSON.stringify(message));
                    })
            })
        });
    };

    const sendMessage = async () => {
        const msg = document.querySelector("#message").value;

        if (msg.trim() !== "") {
            const message = {
                content: msg,
                timestamp: new Date(),
                type: 'comment'
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
            const content = comment.content
            const buttonId = `like-${comment.id}`
            const buttonText = `Like ${comment.liked}`

            const messages = document.querySelector("#messages")
            const message = document.createElement("tr");
            message.innerHTML = getMessage(content, buttonId, buttonText)
            messages.append(message)
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
                    type: "like"
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